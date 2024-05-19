package com.example.shop.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name ="cup")
public class Cup {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id; 

    @Column(name = "name")
    String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "cup_flavour",
        joinColumns = @JoinColumn(name = "cup_id"),
        inverseJoinColumns = @JoinColumn(name = "flavour_id")
    )
    private List<Flavour> flavours;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "cup_toppings",
        joinColumns = @JoinColumn(name = "cup_id"),
        inverseJoinColumns = @JoinColumn(name = "topping_id")
    )
    private List<Topping> toppings;

    @Column(name = "price")
    BigDecimal price;

    public Cup() {
    }

    public Cup(String name, BigDecimal bd) {
        this.name = name; 
        this.price = bd;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flavour> getFlavour() {
        return this.flavours;
    }

    public void setSpoons(List<Flavour> flavours) {
        this.flavours = flavours;
    }
    
    public List<Topping> getToppings() {
        return this.toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal bd) {
        this.price = bd;
    }

}