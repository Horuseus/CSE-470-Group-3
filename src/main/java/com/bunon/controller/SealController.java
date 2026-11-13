package com.bunon.controller;

import com.bunon.service.CurrentUserService;
import com.bunon.service.SealService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/** F17 - craft seal assignment (admin). OWNER: Member 4 */
@Controller
@RequestMapping("/admin/seals")
public class SealController {

    private final SealService sealService;
    private final CurrentUserService currentUser;

    public SealController(SealService sealService, CurrentUserService currentUser) {
        this.sealService = sealService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String page(Model model) {
        model.addAttribute("seals", sealService.activeSeals());
        // TODO F17: add verified weavers + their products to the model for the assignment form
        return "admin/seal-assignment";
    }

    @PostMapping("/weaver")
    public String assignWeaver(@RequestParam Long weaverId, @RequestParam String evidenceNote, Principal principal) {
        sealService.assignToWeaver(weaverId, evidenceNote, currentUser.getUser(principal));
        return "redirect:/admin/seals";
    }

    @PostMapping("/product")
    public String assignProduct(@RequestParam Long productId, @RequestParam String evidenceNote, Principal principal) {
        sealService.assignToProduct(productId, evidenceNote, currentUser.getUser(principal));
        return "redirect:/admin/seals";
    }

    @PostMapping("/{id}/revoke")
    public String revoke(@PathVariable Long id) {
        sealService.revoke(id);
        return "redirect:/admin/seals";
    }
}
