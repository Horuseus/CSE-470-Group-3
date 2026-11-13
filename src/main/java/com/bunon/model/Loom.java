package com.bunon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Loom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Weaver weaver;

    @Column(nullable = false)
    private String name;                 // "Loom 1 - Jamdani frame"

    private String loomType;             // pit loom, frame loom ...
    private int weeklyWorkingHours;      // capacity used by F1
    private boolean active = true;
}
