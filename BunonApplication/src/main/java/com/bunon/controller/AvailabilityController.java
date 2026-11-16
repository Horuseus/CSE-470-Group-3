package com.bunon.controller;

import com.bunon.model.Design;
import com.bunon.model.Loom;
import com.bunon.model.Product;
import com.bunon.model.Weaver;
import com.bunon.repository.DesignRepository;
import com.bunon.repository.LoomRepository;
import com.bunon.repository.ProductRepository;
import com.bunon.service.CurrentWeaverService;
import com.bunon.service.FileStorageService;
import com.bunon.service.PassportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** F5 - Availability & Pre-order Management (also where a weaver adds a product). */
@Controller
public class AvailabilityController {

    private final ProductRepository productRepository;
    private final LoomRepository loomRepository;
    private final DesignRepository designRepository;
    private final PassportService passportService;
    private final FileStorageService storage;
    private final CurrentWeaverService currentWeaver;

    public AvailabilityController(ProductRepository productRepository, LoomRepository loomRepository,
                                  DesignRepository designRepository, PassportService passportService,
                                  FileStorageService storage, CurrentWeaverService currentWeaver) {
        this.productRepository = productRepository;
        this.loomRepository = loomRepository;
        this.designRepository = designRepository;
        this.passportService = passportService;
        this.storage = storage;
        this.currentWeaver = currentWeaver;
    }

    @GetMapping("/availability")
    public String page(Model model) {
        Weaver weaver = currentWeaver.get();
        List<String> categories = new ArrayList<>(Product.WOMEN_CATEGORIES);
        categories.addAll(Product.MEN_CATEGORIES);

        model.addAttribute("products", productRepository.findByWeaverId(weaver.getId()));
        model.addAttribute("looms", loomRepository.findByWeaverId(weaver.getId()));
        model.addAttribute("designs", designRepository.findByWeaverIdOrderByRegisteredOnDesc(weaver.getId()));
        model.addAttribute("categories", categories);
        return "availability";
    }

    @PostMapping("/availability/add")
    public String addProduct(@RequestParam String title, @RequestParam String category,
                             @RequestParam String weaveType, @RequestParam String material,
                             @RequestParam double price, @RequestParam int stockQty,
                             @RequestParam(defaultValue = "false") boolean preorder,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expectedDate,
                             @RequestParam(required = false) Long loomId,
                             @RequestParam(required = false) Long designId,
                             @RequestParam(required = false) MultipartFile image,
                             RedirectAttributes ra) {
        String problem = checkAvailability(stockQty, preorder, expectedDate);
        if (problem == null && price < 0) {
            problem = "Price cannot be negative.";
        }
        if (problem != null) {
            ra.addFlashAttribute("error", problem);
            return "redirect:/availability";
        }

        Product product = new Product();
        product.setTitle(title);
        product.setCategory(category);
        product.setWeaveType(weaveType);
        product.setMaterial(material);
        product.setPrice(price);
        product.updateAvailability(stockQty, expectedDate, preorder);
        product.setWeaver(currentWeaver.get());
        if (loomId != null) {
            Loom loom = loomRepository.findById(loomId).orElse(null);
            if (loom != null) {
                currentWeaver.mustOwn(loom.getWeaver());
            }
            product.setLoom(loom);
        }
        if (designId != null) {
            Design design = designRepository.findById(designId).orElse(null);
            if (design != null) {
                currentWeaver.mustOwn(design.getWeaver());
            }
            product.setDesign(design);
        }
        try {
            if (image != null && !image.isEmpty()) {
                product.setImageFile(storage.save(image));
            }
        } catch (IOException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/availability";
        }

        product = productRepository.save(product);
        passportService.createFor(product);          // every product gets a QR passport (F3)
        ra.addFlashAttribute("message", "Product added. Its QR passport is ready.");
        return "redirect:/availability";
    }

    @PostMapping("/availability/{id}/update")
    public String update(@PathVariable Long id, @RequestParam int stockQty,
                         @RequestParam(defaultValue = "false") boolean preorder,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expectedDate,
                         RedirectAttributes ra) {
        String problem = checkAvailability(stockQty, preorder, expectedDate);
        if (problem != null) {
            ra.addFlashAttribute("error", problem);
        } else {
            Product product = productRepository.findById(id).orElseThrow();
            currentWeaver.mustOwn(product.getWeaver());
            product.updateAvailability(stockQty, expectedDate, preorder);
            productRepository.save(product);
            ra.addFlashAttribute("message", "\"" + product.getTitle() + "\" updated.");
        }
        return "redirect:/availability";
    }

    // Returns an error message, or null when everything is fine.
    private String checkAvailability(int stockQty, boolean preorder, LocalDate expectedDate) {
        if (stockQty < 0) {
            return "Stock cannot be negative.";
        }
        if (preorder && expectedDate == null) {
            return "A pre-order needs an expected completion date.";
        }
        return null;
    }
}
