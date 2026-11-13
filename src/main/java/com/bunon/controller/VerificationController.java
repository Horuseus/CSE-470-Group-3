package com.bunon.controller;

import com.bunon.model.VerificationApplication;
import com.bunon.service.CurrentUserService;
import com.bunon.service.FileStorageService;
import com.bunon.service.VerificationService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/** F16 - admin verification queue. OWNER: Member 1 */
@Controller
@RequestMapping("/admin/verification")
public class VerificationController {

    private final VerificationService verificationService;
    private final CurrentUserService currentUser;
    private final FileStorageService fileStorage;

    public VerificationController(VerificationService verificationService,
                                  CurrentUserService currentUser, FileStorageService fileStorage) {
        this.verificationService = verificationService;
        this.currentUser = currentUser;
        this.fileStorage = fileStorage;
    }

    @GetMapping
    public String queue(Model model) {
        model.addAttribute("applications", verificationService.pendingApplications());
        return "admin/verification-queue";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("application", verificationService.get(id));
        return "admin/verification-detail";
    }

    /** NID image is only visible to admins (path is under /admin/**). */
    @GetMapping("/{id}/nid")
    @ResponseBody
    public ResponseEntity<Resource> nidImage(@PathVariable Long id) {
        VerificationApplication app = verificationService.get(id);
        String path = app.getWeaver().getNidImagePath();
        if (path == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fileStorage.load(path));
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, Principal principal) {
        verificationService.approve(id, currentUser.getUser(principal));
        return "redirect:/admin/verification";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id, @RequestParam String note, Principal principal) {
        verificationService.reject(id, currentUser.getUser(principal), note);
        return "redirect:/admin/verification";
    }

    @PostMapping("/{id}/request-correction")
    public String requestCorrection(@PathVariable Long id, @RequestParam String note, Principal principal) {
        verificationService.requestCorrection(id, currentUser.getUser(principal), note);
        return "redirect:/admin/verification";
    }
}
