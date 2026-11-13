package com.bunon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Minimal stub for Sprint 1: F1 needs it to read the current workload of a loom.
 * It is extended in Sprint 3 (F10 order splitting, F8 milestones) - do NOT rename fields without telling the team.
 */
@Entity
@Getter @Setter @NoArgsConstructor
public class SubOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Weaver weaver;

    @ManyToOne
    private Loom loom;

    private int estimatedHours;          // work still required
    private LocalDate expectedCompletion;

    @Enumerated(EnumType.STRING)
    private SubOrderStatus status = SubOrderStatus.PENDING;
}
