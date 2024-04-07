package com.example.shop.model;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "article")
public class Article {

    @Id 
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "name")
    private String name; 

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @Column(name = "price")
    private BigDecimal price; 

    @Column(name = "description")
    private String description; 

    public Article(){}

    public Article(String name, BigDecimal price, Type type, String description){
        this.name = name; 
        this.price = price;
        this.type = type; 
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }


    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}