package com.bunon.controller;

import com.bunon.model.Role;
import com.bunon.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Login and sign-up pages.
 * Logging in itself is handled by Spring Security (see SecurityConfig): it checks the email and password
 * and then sends the user to /dashboard. We only need to show the page.
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // "?role=CUSTOMER" in the address pre-selects that account type
    @GetMapping("/signup")
    public String signupPage(@RequestParam(defaultValue = "WEAVER") String role, Model model) {
        model.addAttribute("role", role);
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String role, @RequestParam String name, @RequestParam String username,
                         @RequestParam String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam String password, @RequestParam String confirmPassword,
                         @RequestParam(required = false) String district,
                         @RequestParam(required = false) String specialization,
                         @RequestParam(required = false) String deliveryAddress,
                         Model model, RedirectAttributes ra) {
        try {
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("The two passwords do not match.");
            }
            userService.register(Role.valueOf(role), name, username, email, phone, password, district, specialization, deliveryAddress);
            ra.addFlashAttribute("message", "Account created. Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // Show the form again with a message, keeping what the person typed (but not the passwords)
            model.addAttribute("error", e.getMessage());
            model.addAttribute("role", role);
            model.addAttribute("name", name);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            model.addAttribute("district", district);
            model.addAttribute("specialization", specialization);
            model.addAttribute("deliveryAddress", deliveryAddress);
            return "signup";
        }
    }
}
