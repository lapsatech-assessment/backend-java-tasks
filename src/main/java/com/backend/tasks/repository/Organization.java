package com.backend.tasks.repository;

import javax.persistence.*;
import java.util.Set;

/**
 * Implement entity:
 * 1. Map to users
 * 2. Add equals and hashCode methods
 */
@Entity
public class Organization {
    @Id
    private Long id;
    private String name;

    /**
     * Map organization with users.
     * Use OneToMany association and map by organization field in User class.
     * Fetch lazy, cascade all
     */
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
