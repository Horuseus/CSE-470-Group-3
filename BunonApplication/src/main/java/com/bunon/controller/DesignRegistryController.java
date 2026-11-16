package com.bunon.controller;

import com.bunon.model.Design;
import com.bunon.repository.DesignRepository;
import com.bunon.service.CurrentWeaverService;
import com.bunon.service.FileStorageService;
import com.bunon.service.SimilarDesign;
import com.bunon.service.SimilarityService;
import com.bunon.service.SimilarityService.Fingerprint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/** F2 - Design & Motif Registry (register a design + check for look-alikes). */
@Controller
public class DesignRegistryController {

    private final DesignRepository designRepository;
    private final SimilarityService similarityService;
    private final FileStorageService storage;
    private final CurrentWeaverService currentWeaver;

    public DesignRegistryController(DesignRepository designRepository, SimilarityService similarityService,
                                    FileStorageService storage, CurrentWeaverService currentWeaver) {
        this.designRepository = designRepository;
        this.similarityService = similarityService;
        this.storage = storage;
        this.currentWeaver = currentWeaver;
    }

    @GetMapping("/designs")
    public String page(Model model) {
        fillPage(model);
        return "designs";
    }

    // "Check only": compares the picture with all registered designs but saves nothing.
    @PostMapping("/designs/check")
    public String check(@RequestParam MultipartFile image, Model model) {
        try {
            Fingerprint fingerprint = similarityService.analyse(image);
            model.addAttribute("checked", true);
            model.addAttribute("matches", similarityService.findSimilar(fingerprint));
        } catch (IOException e) {
            model.addAttribute("error", e.getMessage());
        }
        fillPage(model);
        return "designs";
    }

    // Register: refuse if it looks like a design that is already registered.
    @PostMapping("/designs/register")
    public String register(@RequestParam String title, @RequestParam MultipartFile image, RedirectAttributes ra) {
        try {
            Fingerprint fingerprint = similarityService.analyse(image);
            List<SimilarDesign> matches = similarityService.findSimilar(fingerprint);

            if (!matches.isEmpty()) {
                SimilarDesign top = matches.get(0);
                ra.addFlashAttribute("error", "Not registered: this looks " + Math.round(top.percent())
                        + "% like \"" + top.design().getTitle() + "\" by " + top.design().getWeaver().getName()
                        + ", registered on " + top.design().getRegisteredOn().toLocalDate()
                        + " (measured by the " + top.method() + ").");
            } else {
                String fileName = storage.save(image);
                Design design = new Design(title, fileName, fingerprint.hash(), LocalDateTime.now(), currentWeaver.get());
                designRepository.save(design);

                ra.addFlashAttribute("message", "Design registered and date-stamped.");
            }
        } catch (IOException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/designs";
    }

    private void fillPage(Model model) {
        model.addAttribute("designs", designRepository.findAllByOrderByRegisteredOnDesc());
    }
}
