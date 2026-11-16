package com.bunon.model;

import jakarta.persistence.*;

@Entity
public class PriceEstimate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double materialCost;
    private double laborHours;
    private double hourlyRate;
    private double otherCost;
    private double total;

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

    public Long getId() { return id; }
    public Weaver getWeaver() { return weaver; }     public void setWeaver(Weaver v) { this.weaver = v; }
    public Product getProduct() { return product; }   public void setProduct(Product v) { this.product = v; }
}
