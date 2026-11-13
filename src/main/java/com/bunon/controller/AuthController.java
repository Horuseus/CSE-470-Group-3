package com.bunon.controller;

import com.bunon.dto.CustomerRegistrationForm;
import com.bunon.dto.WeaverRegistrationForm;
import com.bunon.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * F16 - registration. OWNER: Member 1
 * TODO F16: weaver "my verification status" page + resubmit form when status = CORRECTION_REQUESTED.
 */
@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register/weaver")
    public String weaverForm(Model model) {
        model.addAttribute("form", new WeaverRegistrationForm());
        return "auth/register-weaver";
    }

    @PostMapping("/register/weaver")
    public String registerWeaver(@Valid @ModelAttribute("form") WeaverRegistrationForm form,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register-weaver";
        }
        try {
            authService.registerWeaver(form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register-weaver";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/register/customer")
    public String customerForm(Model model) {
        model.addAttribute("form", new CustomerRegistrationForm());
        return "auth/register-customer";
    }

    @PostMapping("/register/customer")
    public String registerCustomer(@Valid @ModelAttribute("form") CustomerRegistrationForm form,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register-customer";
        }
        try {
            authService.registerCustomer(form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register-customer";
        }
        return "redirect:/login?registered";
    }
}
