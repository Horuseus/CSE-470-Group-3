package com.bunon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * F3: one line in a product's history (e.g. "WOVEN").
 * Each entry stores the hash (fingerprint) of the entry before it, so the entries form a chain.
 * If someone secretly edits an old entry, its hash no longer matches and the chain breaks.
 */
@Entity
public class PassportEntry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProductPassport passport;

    private String eventType;

    @Column(length = 500)
    private String details;

    private LocalDateTime createdAt;
    private String prevHash;     // hash of the previous entry ("START" for the first one)
    private String hash;         // hash of this entry

    public PassportEntry() { }

    public PassportEntry(ProductPassport passport, String eventType, String details, LocalDateTime createdAt, String prevHash) {
        this.passport = passport; this.eventType = eventType; this.details = details;
        this.createdAt = createdAt; this.prevHash = prevHash;
    }

    public Long getId() { return id; }
    public ProductPassport getPassport() { return passport; }
    public String getEventType() { return eventType; }
    public String getDetails() { return details; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPrevHash() { return prevHash; }
    public String getHash() { return hash; }     public void setHash(String v) { this.hash = v; }
}
