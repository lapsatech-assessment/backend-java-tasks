package com.backend.tasks.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements Serializable {

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
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "org_id")
    @JsonIgnore
    private Organization organization;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // CONSTRUCTORS

    @Deprecated // for JPA needs
    protected User() {
    }

    private User(Long id, String username, String password, Organization organization) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.organization = organization;
    }

    // STATIC

    public static User of(Long id, String username, String password, Organization organization) {
        final User user = new User(id, username, password, organization);
        if (organization != null)
            organization.getUsers().add(user);
        return user;
    }

    public static User of(Long id, String username, String password) {
        return of(id, username, password, null);
    }

    public static User of(String username, String password) {
        return of(null, username, password, null);
    }

    public static User of(String username, String password, Organization organization) {
        return of(null, username, password, organization);
    }

    public static User copyOf(Long id, User user) {
        return of(id, user.username, user.password, user.organization);
    }

    public static User copyOf(User user) {
        return copyOf(user.id, user);
    }

    // hC/eq/toSt

    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;

        final User other = (User) obj;

        // id
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;

        // username
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;

        // password
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, username='%s', password='%s']", id, username, password);
    }

}
