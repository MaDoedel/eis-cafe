package com.example.shop.model;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;

@Entity
@Table(name = "flavour")
public class Flavour {

    @Id 
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "name")
    private String name; 

    @Column(name = "isVegan")
    private boolean isVegan; 

    @Column(name = "description")
    private String description; 

    public Flavour(){}

    public Flavour(String name, boolean isVegan, String description){
        this.name = name; 
        this.isVegan = isVegan; 
        this.description = description;
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

    public void setIsVegan(boolean isVegan) {
        this.isVegan = isVegan;
    }

    public boolean getIsVegan() {
        return isVegan;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}