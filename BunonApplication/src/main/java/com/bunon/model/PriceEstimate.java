package com.bunon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PriceEstimate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double materialCost;
    private double laborHours;
    private double hourlyRate;
    private double otherCost;
    private double total;
    private LocalDateTime createdOn = LocalDateTime.now();

    @ManyToOne
    private Weaver weaver;

    @ManyToOne
    private Product product;

    public PriceEstimate() { }

    public PriceEstimate(double materialCost, double laborHours, double hourlyRate, double otherCost) {
        this.materialCost = materialCost; this.laborHours = laborHours;
        this.hourlyRate = hourlyRate; this.otherCost = otherCost;
        calculate();
    }

    public void calculate() {
        this.total = materialCost + (laborHours * hourlyRate) + otherCost;
    }
    
    public double getLaborCost() { return laborHours * hourlyRate; }
    public double getMaterialPercent() { return total == 0 ? 0 : materialCost / total * 100; }
    public double getLaborPercent() { return total == 0 ? 0 : getLaborCost() / total * 100; }
    public double getOtherPercent() { return total == 0 ? 0 : otherCost / total * 100; }

    public Long getId() { return id; }
    public double getMaterialCost() { return materialCost; }
    public double getLaborHours() { return laborHours; }
    public double getHourlyRate() { return hourlyRate; }
    public double getOtherCost() { return otherCost; }
    public double getTotal() { return total; }
    public LocalDateTime getCreatedOn() { return createdOn; }
    public Weaver getWeaver() { return weaver; }     public void setWeaver(Weaver v) { this.weaver = v; }
    public Product getProduct() { return product; }   public void setProduct(Product v) { this.product = v; }
}
