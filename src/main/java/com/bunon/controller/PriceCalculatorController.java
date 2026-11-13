package com.bunon.controller;

import com.bunon.service.CurrentUserService;
import com.bunon.service.PriceCalculatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/** F4 - pricing breakdown calculator. OWNER: Member 3 */
@Controller
@RequestMapping("/weaver/price-calculator")
public class PriceCalculatorController {

    private final PriceCalculatorService calculatorService;
    private final CurrentUserService currentUser;

    public PriceCalculatorController(PriceCalculatorService calculatorService, CurrentUserService currentUser) {
        this.calculatorService = calculatorService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String page(Principal principal, Model model) {
        model.addAttribute("history", calculatorService.historyOf(currentUser.getWeaver(principal)));
        return "weaver/price-calculator";
    }

    // TODO F4: POST /weaver/price-calculator -> calculate, save PriceEstimate, show breakdown
}
