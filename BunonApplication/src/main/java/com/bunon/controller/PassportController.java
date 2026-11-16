package com.bunon.controller;

import com.bunon.model.CraftSeal;
import com.bunon.model.PassportEntry;
import com.bunon.model.Product;
import com.bunon.model.ProductPassport;
import com.bunon.model.Weaver;
import com.bunon.repository.CraftSealRepository;
import com.bunon.repository.PassportEntryRepository;
import com.bunon.repository.ProductPassportRepository;
import com.bunon.repository.ProductRepository;
import com.bunon.service.CurrentWeaverService;
import com.bunon.service.PassportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

/** F3 - QR Product Passport (weaver, loom, materials, finish date, craft seal + tamper-proof history). */
@Controller
public class PassportController {

    public static final List<String> EVENT_TYPES = List.of("YARN_DYED", "WOVEN", "FINISHED", "QUALITY_CHECKED", "PACKED");

    private final ProductRepository productRepository;
    private final ProductPassportRepository passportRepository;
    private final PassportEntryRepository entryRepository;
    private final CraftSealRepository sealRepository;
    private final PassportService passportService;
    private final CurrentWeaverService currentWeaver;
    private final String publicUrl;

    public PassportController(ProductRepository productRepository, ProductPassportRepository passportRepository,
                              PassportEntryRepository entryRepository, CraftSealRepository sealRepository,
                              PassportService passportService, CurrentWeaverService currentWeaver,
                              @Value("${bunon.public-url:}") String publicUrl) {
        this.productRepository = productRepository;
        this.passportRepository = passportRepository;
        this.entryRepository = entryRepository;
        this.sealRepository = sealRepository;
        this.passportService = passportService;
        this.currentWeaver = currentWeaver;
        this.publicUrl = publicUrl;
    }

    // List of products so customers can open a passport. A logged-in weaver only sees their own.
    @GetMapping("/passport")
    public String list(Model model) {
        Weaver mine = currentWeaver.getOrNull();
        model.addAttribute("products", mine != null ? productRepository.findByWeaverId(mine.getId())
                                                    : productRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("myShopOnly", mine != null);
        return "passport-list";
    }

    // The page the QR code opens. "?manage=true" also shows the form for the owner to add history.
    @GetMapping("/passport/{productId}")
    public String show(@PathVariable Long productId, @RequestParam(defaultValue = "false") boolean manage, Model model) {
        Product product = productRepository.findById(productId).orElseThrow();
        ProductPassport passport = passportRepository.findByProductId(productId).orElseThrow();
        List<PassportEntry> entries = entryRepository.findByPassportIdOrderByIdAsc(passport.getId());

        // Finish date = the day the "FINISHED" step was logged (if it was)
        LocalDate finishedOn = null;
        for (PassportEntry entry : entries) {
            if ("FINISHED".equals(entry.getEventType())) {
                finishedOn = entry.getCreatedAt().toLocalDate();
            }
        }
        // Craft seal: one given to this product, otherwise one given to its weaver
        CraftSeal seal = sealRepository.findFirstByProductIdAndStatus(productId, "ACTIVE")
                .orElseGet(() -> sealRepository.findFirstByWeaverIdAndStatus(product.getWeaver().getId(), "ACTIVE").orElse(null));

        // Only the weaver who owns this product may add history (nobody else sees the form)
        Weaver me = currentWeaver.getOrNull();
        boolean isOwner = me != null && product.getWeaver().getId().equals(me.getId());

        model.addAttribute("product", product);
        model.addAttribute("entries", entries);
        model.addAttribute("valid", passportService.verifyChain(passport.getId()));
        model.addAttribute("finishedOn", finishedOn);
        model.addAttribute("seal", seal);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("manage", manage && isOwner);
        model.addAttribute("eventTypes", EVENT_TYPES);
        model.addAttribute("passportUrl", passportUrl(productId));
        return "passport";
    }

    // Returns a PNG picture (not a web page) with the QR code. "?download=true" makes the browser save it.
    @GetMapping("/passport/{productId}/qr.png")
    public ResponseEntity<byte[]> qr(@PathVariable Long productId,
                                     @RequestParam(defaultValue = "false") boolean download) throws Exception {
        byte[] png = passportService.makeQrPng(passportUrl(productId));
        ResponseEntity.BodyBuilder response = ResponseEntity.ok().contentType(MediaType.IMAGE_PNG);
        if (download) {
            response.header("Content-Disposition", "attachment; filename=bunon-passport-" + productId + ".png");
        }
        return response.body(png);
    }

    @PostMapping("/passport/{productId}/entries")
    public String addEntry(@PathVariable Long productId, @RequestParam String eventType,
                           @RequestParam String details, RedirectAttributes ra) {
        Product product = productRepository.findById(productId).orElseThrow();
        currentWeaver.mustOwn(product.getWeaver());
        ProductPassport passport = passportRepository.findByProductId(productId).orElseThrow();
        passportService.addEntry(passport, eventType, details);
        ra.addFlashAttribute("message", "History entry added to the passport.");
        return "redirect:/passport/" + productId + "?manage=true";
    }

    // The address stored in the QR code, e.g. http://localhost:8080/passport/3
    private String passportUrl(Long productId) {
        if (publicUrl != null && !publicUrl.isBlank()) {
            return publicUrl.replaceAll("/+$", "") + "/passport/" + productId;
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/passport/" + productId).toUriString();
    }
}
