package com.bunon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Authenticity badge. Assigned to a weaver OR to a single product (F17). */
@Entity
@Getter @Setter @NoArgsConstructor
public class CraftSeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String sealCode;        // e.g. BUN-2026-0001

    @ManyToOne
    private Weaver weaver;          // set for a weaver seal

    @ManyToOne
    private Product product;        // set for a product seal

    @Column(length = 1000)
    private String evidenceNote;    // what the admin reviewed before assigning

    @ManyToOne(optional = false)
    private User assignedBy;

    private LocalDateTime assignedAt = LocalDateTime.now();
    private boolean revoked = false;
}
