package com.bunon.model;

/** The four kinds of user from the SRS. Spring Security uses these names to decide who may see what. */
public enum Role {
    WEAVER("Weaver"),
    CUSTOMER("Customer"),
    COOPERATIVE_MEMBER("Cooperative member"),
    ADMIN("Administrator");

    private final String label;     // the friendly name shown on screen

    Role(String label) { this.label = label; }

    public String getLabel() { return label; }
}
