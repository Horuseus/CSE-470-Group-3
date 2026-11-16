package com.bunon.controller;

import com.bunon.model.Loom;
import com.bunon.model.Product;
import com.bunon.model.SubOrder;
import com.bunon.repository.LoomRepository;
import com.bunon.repository.ProductRepository;
import com.bunon.repository.SubOrderRepository;
import com.bunon.service.CurrentWeaverService;
import com.bunon.service.LoomScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** F1 - Loom Capacity & Production Scheduler (add / edit / remove looms + estimate a finish date). */
@Controller
public class LoomScheduleController {

    /** One line of the page: a loom, how busy it is, and (if asked) when a new order would finish. */
    public record LoomRow(Loom loom, int pendingHours, LocalDate estimate) { }

    private final LoomRepository loomRepository;
    private final SubOrderRepository subOrderRepository;
    private final ProductRepository productRepository;
    private final LoomScheduleService scheduleService;
    private final CurrentWeaverService currentWeaver;

    public LoomScheduleController(LoomRepository loomRepository, SubOrderRepository subOrderRepository,
                                  ProductRepository productRepository, LoomScheduleService scheduleService,
                                  CurrentWeaverService currentWeaver) {
        this.loomRepository = loomRepository;
        this.subOrderRepository = subOrderRepository;
        this.productRepository = productRepository;
        this.scheduleService = scheduleService;
        this.currentWeaver = currentWeaver;
    }

    // Show the page. "?hours=40" in the address asks for an estimate for a 40-hour order.
    @GetMapping("/looms")
    public String page(@RequestParam(required = false) Integer hours, Model model) {
        List<LoomRow> rows = new ArrayList<>();
        LocalDate bestDate = null;

        for (Loom loom : loomRepository.findByWeaverId(currentWeaver.get().getId())) {
            int pending = scheduleService.pendingHours(loom);
            LocalDate estimate = null;
            if (hours != null && hours > 0) {
                estimate = scheduleService.earliestCompletion(loom, hours);
            }
            rows.add(new LoomRow(loom, pending, estimate));
            if (estimate != null && (bestDate == null || estimate.isBefore(bestDate))) {
                bestDate = estimate;
            }
        }
        model.addAttribute("rows", rows);
        model.addAttribute("hours", hours);
        model.addAttribute("bestDate", bestDate);
        return "looms";
    }

    @PostMapping("/looms/add")
    public String addLoom(@RequestParam String loomType, @RequestParam int weeklyHours, RedirectAttributes ra) {
        if (weeklyHours < 1 || weeklyHours > 168) {
            ra.addFlashAttribute("error", "Weekly hours must be between 1 and 168.");
        } else {
            loomRepository.save(new Loom(loomType, weeklyHours, "ACTIVE", currentWeaver.get()));
            ra.addFlashAttribute("message", "Loom added.");
        }
        return "redirect:/looms";      // "redirect:" = send the browser back to the page
    }

    @PostMapping("/looms/{id}/update")
    public String updateLoom(@PathVariable Long id, @RequestParam String loomType,
                             @RequestParam int weeklyHours, @RequestParam String status, RedirectAttributes ra) {
        if (weeklyHours < 1 || weeklyHours > 168) {
            ra.addFlashAttribute("error", "Weekly hours must be between 1 and 168.");
            return "redirect:/looms";
        }
        Loom loom = loomRepository.findById(id).orElseThrow();
        currentWeaver.mustOwn(loom.getWeaver());          // not someone else's loom
        loom.setLoomType(loomType);
        loom.setWeeklyHours(weeklyHours);
        loom.setStatus(status);
        loomRepository.save(loom);
        ra.addFlashAttribute("message", "Loom saved.");
        return "redirect:/looms";
    }

    @PostMapping("/looms/{id}/delete")
    public String deleteLoom(@PathVariable Long id, RedirectAttributes ra) {
        Loom loom = loomRepository.findById(id).orElseThrow();
        currentWeaver.mustOwn(loom.getWeaver());
        for (Product product : productRepository.findByLoomId(id)) {   // products must forget this loom first
            product.setLoom(null);
            productRepository.save(product);
        }
        loomRepository.delete(loom);
        ra.addFlashAttribute("message", "Loom removed.");
        return "redirect:/looms";
    }

    @PostMapping("/looms/{id}/jobs")
    public String addJob(@PathVariable Long id, @RequestParam String title, @RequestParam int hours, RedirectAttributes ra) {
        if (hours < 1) {
            ra.addFlashAttribute("error", "A job needs at least 1 hour.");
        } else {
            Loom loom = loomRepository.findById(id).orElseThrow();
            currentWeaver.mustOwn(loom.getWeaver());
            subOrderRepository.save(new SubOrder(title, hours, loom));
            ra.addFlashAttribute("message", "Job added to the loom.");
        }
        return "redirect:/looms";
    }

    @PostMapping("/jobs/{id}/done")
    public String finishJob(@PathVariable Long id, RedirectAttributes ra) {
        SubOrder job = subOrderRepository.findById(id).orElseThrow();
        currentWeaver.mustOwn(job.getLoom().getWeaver());
        job.setDone(true);
        subOrderRepository.save(job);
        ra.addFlashAttribute("message", "Job marked as done.");
        return "redirect:/looms";
    }
}
