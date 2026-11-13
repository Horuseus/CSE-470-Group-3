package com.bunon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** F15 - result of the Production Yield Estimator for one loom. */
@Entity
@Getter @Setter @NoArgsConstructor
public class YieldEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Loom loom;

    private BigDecimal yarnKg;               // input: yarn used
    private BigDecimal expectedOutputMeters; // computed
    private BigDecimal wastagePercent;       // computed / input
    private LocalDateTime createdAt = LocalDateTime.now();
}
