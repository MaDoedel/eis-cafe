package com.example.shop.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Fruit")
public class Fruit extends Topping {

    public Fruit() {
    }

    public Fruit(String name, String description) {
        super(name, description, true);
    }

    @Override
    public boolean isFruit(){return true;}
        
}