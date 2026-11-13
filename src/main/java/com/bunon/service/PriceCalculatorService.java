package com.bunon.service;

import com.bunon.model.PriceEstimate;
import com.bunon.model.Weaver;
import com.bunon.repository.PriceEstimateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * F4 - Pricing Breakdown Calculator.
 * OWNER: Member 3
 */
@Service
public class PriceCalculatorService {

    private final PriceEstimateRepository estimateRepository;

    public PriceCalculatorService(PriceEstimateRepository estimateRepository) {
        this.estimateRepository = estimateRepository;
    }

    public List<PriceEstimate> historyOf(Weaver weaver) {
        return estimateRepository.findByWeaverOrderByCreatedAtDesc(weaver);
    }

    /** price = (materialCost + laborHours * hourlyRate + otherExpenses) * (1 + margin/100) */
    public BigDecimal calculate(BigDecimal materialCost, BigDecimal laborHours, BigDecimal hourlyRate,
                                BigDecimal otherExpenses, BigDecimal profitMarginPercent) {
        // TODO F4: implement with BigDecimal (scale 2, HALF_UP); reject negative inputs
        throw new UnsupportedOperationException("TODO F4");
    }
}
