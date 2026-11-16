package com.bunon.model;

import jakarta.persistence.*;

/**
 * A piece of work waiting on a loom. The scheduler adds up the hours of unfinished jobs
 * to find out how busy a loom is. (Feature F10 will later add price/commission fields here.)
 */
@Entity
public class SubOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int hoursRequired;
    private boolean done;

    @ManyToOne
    private Loom loom;

    public SubOrder() { }

    public SubOrder(String title, int hoursRequired, Loom loom) {
        this.title = title; this.hoursRequired = hoursRequired; this.loom = loom;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public int getHoursRequired() { return hoursRequired; }
    public boolean isDone() { return done; }     public void setDone(boolean v) { this.done = v; }
    public Loom getLoom() { return loom; }
}
