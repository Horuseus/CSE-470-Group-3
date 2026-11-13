package com.bunon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class VerificationApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Weaver weaver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status = VerificationStatus.PENDING;

    @Column(length = 1000)
    private String adminNote; // reason for rejection / what to correct

    private LocalDateTime submittedAt = LocalDateTime.now();
    private LocalDateTime reviewedAt;

    @ManyToOne
    private User reviewedBy;
}
