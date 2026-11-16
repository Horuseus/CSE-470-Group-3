package com.bunon.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** F1: one loom owned by a weaver, with the hours per week it can run. */
@Entity
public class Loom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loomType;        // e.g. "Pit loom"
    private int weeklyHours;        // working hours per week
    private String status = "ACTIVE";   // ACTIVE or MAINTENANCE

    @ManyToOne                       // many looms belong to one weaver
    private Weaver weaver;

    // One loom has many jobs. If the loom is deleted, its jobs go too.
    @OneToMany(mappedBy = "loom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubOrder> jobs = new ArrayList<>();

    public Loom() { }

    public Loom(String loomType, int weeklyHours, String status, Weaver weaver) {
        this.loomType = loomType; this.weeklyHours = weeklyHours; this.status = status; this.weaver = weaver;
    }

    public Long getId() { return id; }
    public String getLoomType() { return loomType; }           public void setLoomType(String v) { this.loomType = v; }
    public int getWeeklyHours() { return weeklyHours; }        public void setWeeklyHours(int v) { this.weeklyHours = v; }
    public String getStatus() { return status; }               public void setStatus(String v) { this.status = v; }
    public Weaver getWeaver() { return weaver; }
    public List<SubOrder> getJobs() { return jobs; }
}
