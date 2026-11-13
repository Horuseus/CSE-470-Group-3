package com.bunon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Weaver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String location;        // district / region (used for weaver matching in F6)
    private String specialization;  // e.g. Jamdani, Nakshi Kantha, Muslin

    @Column(unique = true)
    private String nidNumber;
    private String nidImagePath;    // relative path inside upload dir (admin-only access!)

    /** True only after an admin approves the VerificationApplication. */
    private boolean verified = false;
}
