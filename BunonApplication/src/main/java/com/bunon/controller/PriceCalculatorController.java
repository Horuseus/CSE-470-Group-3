package com.bunon.controller;

import com.bunon.model.PriceEstimate;
import com.bunon.model.Product;
import com.bunon.repository.PriceEstimateRepository;
import com.bunon.repository.ProductRepository;
import com.bunon.service.CurrentWeaverService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PriceCalculatorController {

    private final PriceEstimateRepository estimateRepository;
    private final ProductRepository productRepository;
    private final CurrentWeaverService currentWeaver;

    public PriceCalculatorController(PriceEstimateRepository estimateRepository, ProductRepository productRepository,
                                     CurrentWeaverService currentWeaver) {
        this.estimateRepository = estimateRepository;
        this.productRepository = productRepository;
        this.currentWeaver = currentWeaver;
    }

    @GetMapping("/price")
    public String page(@RequestParam(required = false) Long estimateId, Model model) {
        if (estimateId != null) {
            PriceEstimate estimate = estimateRepository.findById(estimateId).orElseThrow();
            currentWeaver.mustOwn(estimate.getWeaver());
            model.addAttribute("estimate", estimate);
        }
        model.addAttribute("products", productRepository.findByWeaverId(currentWeaver.get().getId()));
        model.addAttribute("recent", estimateRepository.findTop5ByWeaverIdOrderByIdDesc(currentWeaver.get().getId()));
        return "price";
    }

    @PostMapping("/price/calculate")
    public String calculate(@RequestParam double materialCost, @RequestParam double laborHours,
                            @RequestParam double hourlyRate, @RequestParam(defaultValue = "0") double otherCost,
                            @RequestParam(required = false) Long productId, RedirectAttributes ra) {
        if (materialCost < 0 || laborHours < 0 || hourlyRate < 0 || otherCost < 0) {
            ra.addFlashAttribute("error", "Costs and hours cannot be negative.");
            return "redirect:/price";
        }
        PriceEstimate estimate = new PriceEstimate(materialCost, laborHours, hourlyRate, otherCost);
        estimate.setWeaver(currentWeaver.get());
        if (productId != null) {
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                currentWeaver.mustOwn(product.getWeaver());
            }
            estimate.setProduct(product);
        }
        estimate = estimateRepository.save(estimate);
        return "redirect:/price?estimateId=" + estimate.getId();
    }

    @PostMapping("/price/{id}/apply")
    public String apply(@PathVariable Long id, RedirectAttributes ra) {
        PriceEstimate estimate = estimateRepository.findById(id).orElseThrow();
        currentWeaver.mustOwn(estimate.getWeaver());
        Product product = estimate.getProduct();
        if (product != null) {
            product.setPrice(Math.round(estimate.getTotal()));
            productRepository.save(product);
            ra.addFlashAttribute("message", "\"" + product.getTitle() + "\" now costs the estimated price.");
        }
        return "redirect:/price?estimateId=" + id;
    }
}
