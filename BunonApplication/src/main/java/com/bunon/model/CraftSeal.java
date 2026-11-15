package com.bunon.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * An "Authentic Handloom Craft Seal". An administrator awards it to a weaver and/or a product
 * after checking evidence. The product passport (F3) shows it.
 */
@Entity
public class CraftSeal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String level;                 // e.g. Gold
    @Column(length = 500)
    private String evidence;              // what was checked
    private LocalDate issuedOn;
    private String status = "ACTIVE";     // ACTIVE or REVOKED

    @ManyToOne private Weaver weaver;     // sealed weaver (optional)
    @ManyToOne private Product product;   // sealed product (optional)

    public CraftSeal() { }

    public CraftSeal(String level, String evidence, LocalDate issuedOn, Weaver weaver, Product product) {
        this.level = level; this.evidence = evidence; this.issuedOn = issuedOn;
        this.weaver = weaver; this.product = product;
    }

    public Long getId() { return id; }
    public String getLevel() { return level; }
    public String getEvidence() { return evidence; }
    public LocalDate getIssuedOn() { return issuedOn; }
    public String getStatus() { return status; }
    public Weaver getWeaver() { return weaver; }
    public Product getProduct() { return product; }
}
