package com.bunon.controller;

import com.bunon.model.User;
import com.bunon.repository.UserRepository;
import com.bunon.repository.WeaverRepository;
import com.bunon.service.FileStorageService;
import com.bunon.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/** "My account": change profile picture, name, username, phone, shop location and password. Works for every role. */
@Controller
public class AccountController {

    private final UserRepository userRepository;
    private final WeaverRepository weaverRepository;
    private final UserService userService;
    private final FileStorageService storage;

    public AccountController(UserRepository userRepository, WeaverRepository weaverRepository,
                             UserService userService, FileStorageService storage) {
        this.userRepository = userRepository;
        this.weaverRepository = weaverRepository;
        this.userService = userService;
        this.storage = storage;
    }

    @GetMapping("/account")
    public String page(Authentication auth, Model model) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("weaver", weaverRepository.findByUserEmail(user.getEmail()).orElse(null));   // null for customers/admins
        return "account";
    }

    @PostMapping("/account/profile")
    public String saveProfile(Authentication auth, @RequestParam String name, @RequestParam String username,
                              @RequestParam(required = false) String phone,
                              @RequestParam(required = false) String district,
                              @RequestParam(required = false) String shopAddress,
                              @RequestParam(required = false) String bio,
                              @RequestParam(required = false) String deliveryAddress,
                              @RequestParam(required = false) MultipartFile photo,
                              RedirectAttributes ra) {
        try {
            String newPhoto = null;
            if (photo != null && !photo.isEmpty()) {
                newPhoto = storage.save(photo);
            }
            userService.updateProfile(auth.getName(), name, username, phone, district, shopAddress, bio, deliveryAddress, newPhoto);
            ra.addFlashAttribute("message", "Your account was updated.");
        } catch (IllegalArgumentException | IOException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account";
    }

    @PostMapping("/account/password")
    public String changePassword(Authentication auth, @RequestParam String currentPassword,
                                 @RequestParam String newPassword, @RequestParam String confirmPassword,
                                 RedirectAttributes ra) {
        try {
            userService.changePassword(auth.getName(), currentPassword, newPassword, confirmPassword);
            ra.addFlashAttribute("message", "Password changed.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account";
    }
}
