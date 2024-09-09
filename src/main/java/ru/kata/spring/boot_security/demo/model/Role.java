package ru.kata.spring.boot_security.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "role_name")
    private String name;
    @JsonBackReference
    @ManyToMany(mappedBy = "allRoles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> allUsers = new HashSet<>();

    public Role() {
    }

    public Role(int id, String name, Set<User> allUsers) {
        this.id = id;
        this.name = name;
        this.allUsers = allUsers;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<User> getAllUsers() {
        return allUsers;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return getId() == role.getId() && Objects.equals(getName(), role.getName()) && Objects.equals(getAllUsers(), role.getAllUsers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAllUsers());
    }
}