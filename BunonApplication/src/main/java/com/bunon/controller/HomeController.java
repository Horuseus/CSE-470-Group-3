package com.bunon.controller;

import com.bunon.model.Product;
import com.bunon.model.Weaver;
import com.bunon.repository.ProductRepository;
import com.bunon.service.CurrentWeaverService;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * "/"     = the landing page for visitors who are not logged in (history, areas, struggle).
 *           Logged-in users are sent to their own dashboard instead.
 * "/shop" = the shop. A weaver who is logged in only sees THEIR OWN products here.
 */
@Controller
public class HomeController {

    private final ProductRepository productRepository;
    private final CurrentWeaverService currentWeaver;

    public HomeController(ProductRepository productRepository, CurrentWeaverService currentWeaver) {
        this.productRepository = productRepository;
        this.currentWeaver = currentWeaver;
    }

    @GetMapping("/")
    public String landing(Authentication auth) {
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }
        return "landing";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String category, Model model) {
        boolean filtered = category != null && !category.isBlank();
        Weaver mine = currentWeaver.getOrNull();      // null unless a weaver is logged in

        List<Product> products;
        if (mine != null) {
            products = filtered ? productRepository.findByWeaverIdAndCategory(mine.getId(), category)
                                : productRepository.findByWeaverId(mine.getId());
        } else {
            products = filtered ? productRepository.findByCategory(category)
                                : productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }
        model.addAttribute("products", products);
        model.addAttribute("category", category);
        model.addAttribute("myShopOnly", mine != null);
        model.addAttribute("womenCategories", Product.WOMEN_CATEGORIES);
        model.addAttribute("menCategories", Product.MEN_CATEGORIES);
        return "shop";
    }
}
