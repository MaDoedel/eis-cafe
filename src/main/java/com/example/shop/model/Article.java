package com.example.shop.model;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;

@Entity
@Table(name = "article")
public class Article {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name; 

    @Column(name = "brand")
    private String brand; 

    @Column(name = "description")
    private String description; 

    public Article(){}

    public Article(String name, String brand, String description){
        this.name = name; 
        this.brand = brand; 
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

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}