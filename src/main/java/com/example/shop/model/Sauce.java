package com.example.shop.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Sauce")
public class Sauce extends Topping {

    public Sauce() {
    }

    public Sauce(String name, String description, boolean isVegan) {
        super(name, description, isVegan);
    }

    @Override
    public boolean isSauce(){return true;}

}