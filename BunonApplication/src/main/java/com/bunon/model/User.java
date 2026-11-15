package com.bunon.model;

import jakarta.persistence.*;

/**
 * Everyone who can log in. The "role" says what kind of user they are.
 * Weavers and cooperative members also get a Weaver profile (see Weaver.java).
 * Note: the table is called app_user because "user" is a reserved word in SQL.
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)          // two accounts cannot share an email
    private String email;

    private String phone;
    private String passwordHash;    // we NEVER store the real password, only a scrambled (BCrypt) version

    @Enumerated(EnumType.STRING)    // saved as text, e.g. "WEAVER"
    private Role role;

    private String deliveryAddress; // used by customers

    @Column(unique = true)
    private String username;        // a short handle, e.g. "rahima_tangail". Can be used to log in.

    private String profileImage;    // file name of the profile picture inside the uploads folder

    public User() { }

    public User(String name, String email, String phone, String passwordHash, Role role) {
        this.name = name; this.email = email; this.phone = phone; this.passwordHash = passwordHash; this.role = role;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public String getDeliveryAddress() { return deliveryAddress; }   public void setDeliveryAddress(String v) { this.deliveryAddress = v; }
    public String getUsername() { return username; }                 public void setUsername(String v) { this.username = v; }
    public String getProfileImage() { return profileImage; }         public void setProfileImage(String v) { this.profileImage = v; }
    public void setName(String v) { this.name = v; }
    public void setPhone(String v) { this.phone = v; }
    public void setPasswordHash(String v) { this.passwordHash = v; }
}
