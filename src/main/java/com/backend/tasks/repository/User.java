package com.backend.tasks.repository;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Implement entity:
 * 1. Map to organization
 * 2. Add equals and hashCode methods
 */
@Entity
public class User {
    @Id
    private Long id;
    private String username;
    private String password;

    /**
     * Map user with organization by org_id field.
     * Use ManyToOne association.
     */
    private Organization organization;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
