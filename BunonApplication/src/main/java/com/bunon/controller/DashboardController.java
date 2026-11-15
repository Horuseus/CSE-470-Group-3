package com.bunon.controller;

import com.bunon.model.Loom;
import com.bunon.model.Role;
import com.bunon.model.User;
import com.bunon.model.Weaver;
import com.bunon.repository.*;
import com.bunon.service.LoomScheduleService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * After login everyone lands on /dashboard, but each role gets its OWN page design:
 * weaver, cooperative member, customer and administrator each have their own template.
 */
@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final WeaverRepository weaverRepository;
    private final ProductRepository productRepository;
    private final LoomRepository loomRepository;
    private final DesignRepository designRepository;
    private final LoomScheduleService scheduleService;

    public DashboardController(UserRepository userRepository, WeaverRepository weaverRepository,
                               ProductRepository productRepository, LoomRepository loomRepository,
                               DesignRepository designRepository,
                               LoomScheduleService scheduleService) {
        this.userRepository = userRepository;
        this.weaverRepository = weaverRepository;
        this.productRepository = productRepository;
        this.loomRepository = loomRepository;
        this.designRepository = designRepository;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("user", user);

        if (user.getRole() == Role.WEAVER) {
            addWeaverNumbers(user, model);
            return "dashboard-weaver";
        } else if (user.getRole() == Role.COOPERATIVE_MEMBER) {
            addWeaverNumbers(user, model);
            return "dashboard-coop";
        } else if (user.getRole() == Role.CUSTOMER) {
            model.addAttribute("latest", productRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream().limit(8).toList());
            return "dashboard-customer";
        } else {
            addAdminNumbers(model);
            return "dashboard-admin";
        }
    }

    // Administrators only (see SecurityConfig)
    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin-users";
    }

    private void addWeaverNumbers(User user, Model model) {
        Weaver weaver = weaverRepository.findByUserEmail(user.getEmail()).orElseThrow();
        List<Loom> looms = loomRepository.findByWeaverId(weaver.getId());
        int pendingHours = 0;
        for (Loom loom : looms) {
            pendingHours += scheduleService.pendingHours(loom);
        }
        model.addAttribute("weaver", weaver);
        model.addAttribute("products", productRepository.findByWeaverId(weaver.getId()));    // ONLY this weaver's products
        model.addAttribute("loomCount", looms.size());
        model.addAttribute("pendingHours", pendingHours);
        model.addAttribute("designCount", designRepository.findByWeaverIdOrderByRegisteredOnDesc(weaver.getId()).size());
    }

    private void addAdminNumbers(Model model) {
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("weaverCount", userRepository.countByRole(Role.WEAVER));
        model.addAttribute("coopCount", userRepository.countByRole(Role.COOPERATIVE_MEMBER));
        model.addAttribute("customerCount", userRepository.countByRole(Role.CUSTOMER));
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("loomCount", loomRepository.count());
        model.addAttribute("designCount", designRepository.count());
        model.addAttribute("recentUsers", userRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream().limit(6).toList());
    }
}
