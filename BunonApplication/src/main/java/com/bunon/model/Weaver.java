package com.bunon.model;

import jakarta.persistence.*;

/**
 * @Entity = "this class is a database table". Each field becomes a column.
 * (Login and the User/Customer/Administrator classes come in a later feature.
 *  For now the app pretends the one seeded weaver is logged in.)
 */
@Entity
public class Weaver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // database numbers the rows 1, 2, 3...
    private Long id;

    private String name;
    private String district;
    private String specialization;

    @Column(length = 500)
    private String bio;

    private String shopAddress;      // where the weaver's shop / workshop is

    @OneToOne                        // the login account this weaver profile belongs to
    private User user;

    public Weaver() { }   // JPA needs an empty constructor

    public Weaver(String name, String district, String specialization, String bio) {
        this.name = name; this.district = district; this.specialization = specialization; this.bio = bio;
    }

    public Weaver(String name, String district, String specialization, String bio, User user) {
        this(name, district, specialization, bio);
        this.user = user;
    }

    public User getUser() { return user; }          public void setUser(User v) { this.user = v; }
    public String getShopAddress() { return shopAddress; }
    public void setName(String v) { this.name = v; }
    public void setDistrict(String v) { this.district = v; }
    public void setSpecialization(String v) { this.specialization = v; }
    public void setBio(String v) { this.bio = v; }
    public void setShopAddress(String v) { this.shopAddress = v; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDistrict() { return district; }
    public String getSpecialization() { return specialization; }
    public String getBio() { return bio; }
}
