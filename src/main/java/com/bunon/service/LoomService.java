package com.bunon.service;

import com.bunon.model.Loom;
import com.bunon.model.Weaver;
import com.bunon.repository.LoomRepository;
import com.bunon.repository.SubOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * F1 - Loom Capacity & Production Scheduler.
 * OWNER: Member 2
 */
@Service
public class LoomService {

    private final LoomRepository loomRepository;
    private final SubOrderRepository subOrderRepository;

    public LoomService(LoomRepository loomRepository, SubOrderRepository subOrderRepository) {
        this.loomRepository = loomRepository;
        this.subOrderRepository = subOrderRepository;
    }

    public List<Loom> loomsOf(Weaver weaver) {
        return loomRepository.findByWeaver(weaver);
    }

    public Loom save(Loom loom) {
        // TODO F1: validate weeklyWorkingHours (1..84), name not blank
        return loomRepository.save(loom);
    }

    public void delete(Long loomId, Weaver owner) {
        // TODO F1: make sure the loom belongs to `owner`; block delete if it has active SubOrders
    }

    /**
     * Earliest realistic completion date for new work needing `requiredHours`.
     * Idea: workload = sum(estimatedHours) of PENDING/IN_PRODUCTION SubOrders on the loom;
     *       weeks = ceil((workload + requiredHours) / weeklyWorkingHours); date = today + weeks * 7.
     * Pick the loom that finishes first.
     */
    public LocalDate earliestCompletion(Weaver weaver, int requiredHours) {
        // TODO F1
        throw new UnsupportedOperationException("TODO F1");
    }
}
