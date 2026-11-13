package com.bunon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Weaver weaver;

    @ManyToOne
    private Loom loom;                       // which loom made it (used by F3 passport in Sprint 2)

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;
    private String category;                 // saree, shawl, fabric ...
    private BigDecimal price;
    private String imagePath;

    // ---- F5: Availability & Pre-order ----
    private int stockQuantity = 0;

    @Enumerated(EnumType.STRING)
    private AvailabilityType availabilityType = AvailabilityType.IN_STOCK;

    private LocalDate expectedCompletionDate; // only meaningful for PRE_ORDER
}
