package com.bunon.service;

import com.bunon.model.Loom;
import com.bunon.model.SubOrder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/** F1 logic: how busy is a loom, and when could a new order be finished? */
@Service
public class LoomScheduleService {

    /** Total hours of work still waiting on this loom. */
    public int pendingHours(Loom loom) {
        int total = 0;
        for (SubOrder job : loom.getJobs()) {
            if (!job.isDone()) {
                total += job.getHoursRequired();
            }
        }
        return total;
    }

    /**
     * Earliest finish date = today + (waiting hours + new order hours) / weekly hours.
     * Example: 40 h waiting + 20 h new = 60 h. A loom running 30 h/week needs 2 weeks = 14 days.
     * Returns null when the loom is not ACTIVE or has no weekly hours.
     */
    public LocalDate earliestCompletion(Loom loom, int newOrderHours) {
        if (!"ACTIVE".equals(loom.getStatus()) || loom.getWeeklyHours() <= 0) {
            return null;
        }
        int totalHours = pendingHours(loom) + newOrderHours;
        double weeks = (double) totalHours / loom.getWeeklyHours();
        long days = (long) Math.ceil(weeks * 7);
        return LocalDate.now().plusDays(days);
    }
}
