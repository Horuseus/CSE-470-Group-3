package com.bunon.controller;

import com.bunon.service.AvailabilityService;
import com.bunon.service.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/** F5 - availability & pre-order management. OWNER: Member 3 */
@Controller
@RequestMapping("/weaver/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final CurrentUserService currentUser;

    public AvailabilityController(AvailabilityService availabilityService, CurrentUserService currentUser) {
        this.availabilityService = availabilityService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String page(Principal principal, Model model) {
        model.addAttribute("products", availabilityService.productsOf(currentUser.getWeaver(principal)));
        return "weaver/availability";
    }

    // TODO F5: POST /weaver/availability/products (create basic product),
    //          POST /weaver/availability/{id} (update stock / type / expected date)
}
