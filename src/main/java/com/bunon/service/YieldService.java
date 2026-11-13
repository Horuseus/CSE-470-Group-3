package com.bunon.service;

import com.bunon.model.Loom;
import com.bunon.model.YieldEstimate;
import com.bunon.repository.YieldEstimateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * F15 - Production Yield Estimator per Loom.
 * OWNER: Member 4 (stretch goal - move to Sprint 2 if time is short)
 */
@Service
public class YieldService {

    private final YieldEstimateRepository yieldRepository;

    public YieldService(YieldEstimateRepository yieldRepository) {
        this.yieldRepository = yieldRepository;
    }

    public List<YieldEstimate> historyOf(Loom loom) {
        return yieldRepository.findByLoomOrderByCreatedAtDesc(loom);
    }

    public YieldEstimate estimate(Loom loom, BigDecimal yarnKg, BigDecimal wastagePercent) {
        // TODO F15: expectedOutputMeters = (yarnKg * (1 - wastage/100)) * metersPerKg (decide constant per loomType)
        throw new UnsupportedOperationException("TODO F15");
    }
}
