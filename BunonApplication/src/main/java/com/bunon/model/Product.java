package com.bunon.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Product {

    public static final List<String> WOMEN_CATEGORIES = List.of("Saree", "Kantha", "Salwar Kameez", "Scarf");
    public static final List<String> MEN_CATEGORIES = List.of("Panjabi", "Fatua", "Lungi", "Gamcha");

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private String weaveType;      // e.g. Jamdani, Khadi
    private String material;
    private double price;          // in Taka
    private int stockQty;
    private LocalDate expectedDate;   // when it will be ready (for pre-orders)
    private boolean preorder;
    private String imageFile;         // optional photo (file name in uploads folder)

    @ManyToOne private Weaver weaver;
    @ManyToOne private Loom loom;       // optional: which loom made it (shown on the passport)
    @ManyToOne private Design design;   // optional: which registered design it uses

    public void updateAvailability(int stockQty, LocalDate expectedDate, boolean preorder) {
        this.stockQty = stockQty; this.expectedDate = expectedDate; this.preorder = preorder;
    }

    public String getAvailabilityLabel() {
        if (preorder) return "Pre-order";
        if (stockQty > 0) return "In stock";
        return "Sold out";
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }             public void setTitle(String v) { this.title = v; }
    public String getCategory() { return category; }       public void setCategory(String v) { this.category = v; }
    public String getWeaveType() { return weaveType; }     public void setWeaveType(String v) { this.weaveType = v; }
    public String getMaterial() { return material; }      public void setMaterial(String v) { this.material = v; }
    public double getPrice() { return price; }             public void setPrice(double v) { this.price = v; }
    public int getStockQty() { return stockQty; }
    public LocalDate getExpectedDate() { return expectedDate; }
    public boolean isPreorder() { return preorder; }
    public String getImageFile() { return imageFile; }     public void setImageFile(String v) { this.imageFile = v; }
    public Weaver getWeaver() { return weaver; }           public void setWeaver(Weaver v) { this.weaver = v; }
    public Loom getLoom() { return loom; }                 public void setLoom(Loom v) { this.loom = v; }
    public Design getDesign() { return design; }           public void setDesign(Design v) { this.design = v; }
}
