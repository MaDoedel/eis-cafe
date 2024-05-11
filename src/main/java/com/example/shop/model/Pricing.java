package com.example.shop.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

import org.hibernate.annotations.GenericGenerator;


// flexible pricing database 
@Entity
@Table(name = "pricing")
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    @NotBlank(message = "Nothing is free")
    private String description;

    @Column(name = "price")
    @NotBlank(message = "Nothing is free")
    private BigDecimal price;


    public Pricing() {
    }

    public Pricing(String description, BigDecimal price) {
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String des) {
        this.description = des;
    }

}
