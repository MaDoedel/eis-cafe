package com.example.shop.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "app_user")
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "name")
    @NotBlank(message = "Invalid name")
    private String name; 

    @Column(name = "surname")
    @NotBlank(message = "Invalid surname")
    private String surname; 

    @Column(name = "email")
    @NotBlank(message = "Invalid email")
    private String email; 

    @Column(name = "state")
    @NotBlank(message = "Invalid state")
    private String state; 

    protected User() {}

    public User(String name, String surname, String email, String state) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.state = state;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setState(String email) {
        this.state = email;
    }

    public String getState() {
        return state;
    }
}
