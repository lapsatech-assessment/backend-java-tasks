package com.backend.tasks.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "organization", orphanRemoval = false)
    @JsonIgnore
    private Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    // no setUsers is required

    // CONSTRUCTORS

    @Deprecated // for JPA needs
    protected Organization() {
    }

    private Organization(Long id, String name) {
        this.id = id;
        this.name = name;
        this.users = new HashSet<>();
    }

    // STATIC

    public static Organization of(Long id, String name) {
        return new Organization(id, name);
    }

    public static Organization of(String name) {
        return of(null, name);
    }

    public static Organization copyOf(Long id, Organization user) {
        return new Organization(id, user.name);
    }

    public static Organization copyOf(Organization user) {
        return copyOf(user.id, user);
    }

    // hC/eq/toSt

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Organization))
            return false;

        final Organization other = (Organization) obj;

        // id
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;

        // name
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return String.format("Organization[id=%d, name='%s']", id, name);
    }
}
