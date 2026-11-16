package com.bunon.model;

import jakarta.persistence.*;

/** F3: the "passport" of one product. Its history lives in PassportEntry rows. */
@Entity
public class ProductPassport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne                       // exactly one passport per product
    private Product product;

    public ProductPassport() { }
    public ProductPassport(Product product) { this.product = product; }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
}
