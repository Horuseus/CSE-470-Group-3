package com.bunon.controller;

import com.bunon.service.CurrentUserService;
import com.bunon.service.LoomService;
import com.bunon.service.YieldService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/** F15 - yield estimator per loom. OWNER: Member 4 (stretch) */
@Controller
@RequestMapping("/weaver/yield")
public class YieldController {

    private final YieldService yieldService;
    private final LoomService loomService;
    private final CurrentUserService currentUser;

    public YieldController(YieldService yieldService, LoomService loomService, CurrentUserService currentUser) {
        this.yieldService = yieldService;
        this.loomService = loomService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String page(Principal principal, Model model) {
        model.addAttribute("looms", loomService.loomsOf(currentUser.getWeaver(principal)));
        return "weaver/yield-estimator";
    }

    // TODO F15: POST /weaver/yield  (loomId, yarnKg, wastagePercent) -> yieldService.estimate(...)
}
