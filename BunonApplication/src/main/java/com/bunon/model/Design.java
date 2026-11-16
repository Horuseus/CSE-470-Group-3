package com.bunon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** F2: an original design/motif a weaver has registered. The date stamp proves who registered it first. */
@Entity
public class Design {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String motifImage;                 // file name inside the uploads folder

    @Column(length = 300)
    private String fingerprint;                // short "signature" of the picture, used to spot look-alikes

    private LocalDateTime registeredOn;

    @ManyToOne
    private Weaver weaver;

    public Design() { }

    public Design(String title, String motifImage, String fingerprint, LocalDateTime registeredOn, Weaver weaver) {
        this.title = title; this.motifImage = motifImage; this.fingerprint = fingerprint;
        this.registeredOn = registeredOn; this.weaver = weaver;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getMotifImage() { return motifImage; }
    public String getFingerprint() { return fingerprint; }
    public LocalDateTime getRegisteredOn() { return registeredOn; }
    public Weaver getWeaver() { return weaver; }
}
