package com.example.shop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "spoon")
public class Spoon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "flavour_id")
    private Flavour flavour;

    @ManyToOne
    @JoinColumn(name = "price_id")
    private Pricing pricing;


    public Spoon() {
    }

    public Spoon(Flavour flavour, Pricing pricing) {
        this.flavour = flavour;
        this.pricing = pricing;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flavour getFlavour() {
        return this.flavour;
    }

    public void setFlavour(Flavour flavour) {
        this.flavour = flavour;
    }

    public Pricing getPricing() {
        return this.pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }
}   
