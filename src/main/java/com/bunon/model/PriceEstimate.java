package com.bunon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** F4 - saved result of the Pricing Breakdown Calculator. */
@Entity
@Getter @Setter @NoArgsConstructor
public class PriceEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Weaver weaver;

    @ManyToOne
    private Product product;             // optional link

    private BigDecimal materialCost;
    private BigDecimal laborHours;
    private BigDecimal hourlyRate;
    private BigDecimal otherExpenses;
    private BigDecimal profitMarginPercent;
    private BigDecimal estimatedPrice;   // computed by PriceCalculatorService

    private LocalDateTime createdAt = LocalDateTime.now();
}
