package com.bunon.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Loom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loomType;        
    private int weeklyHours;        
    private String status = "ACTIVE";   
    @ManyToOne                       
    private Weaver weaver;

    
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
