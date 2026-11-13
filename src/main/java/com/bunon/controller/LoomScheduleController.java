package com.bunon.controller;

import com.bunon.model.Loom;
import com.bunon.service.CurrentUserService;
import com.bunon.service.LoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/** F1 - looms CRUD + earliest completion estimate. OWNER: Member 2 */
@Controller
@RequestMapping("/weaver/looms")
public class LoomScheduleController {

    private final LoomService loomService;
    private final CurrentUserService currentUser;

    public LoomScheduleController(LoomService loomService, CurrentUserService currentUser) {
        this.loomService = loomService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String page(Principal principal, Model model) {
        model.addAttribute("looms", loomService.loomsOf(currentUser.getWeaver(principal)));
        return "weaver/loom-schedule";
    }

    @PostMapping
    public String add(@RequestParam String name, @RequestParam(required = false) String loomType,
                      @RequestParam int weeklyWorkingHours, Principal principal) {
        Loom loom = new Loom();
        loom.setWeaver(currentUser.getWeaver(principal));
        loom.setName(name);
        loom.setLoomType(loomType);
        loom.setWeeklyWorkingHours(weeklyWorkingHours);
        loomService.save(loom);
        return "redirect:/weaver/looms";
    }

    // TODO F1: POST /{id}/edit, POST /{id}/delete, GET /estimate?hours=NN  (calls loomService.earliestCompletion)
}
