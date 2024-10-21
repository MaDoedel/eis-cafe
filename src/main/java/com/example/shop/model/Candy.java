package com.example.shop.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Candy")
public class Candy extends Topping {

    public Candy() {}

    public Candy(String name, String description, boolean isVegan) {
        super(name, description, isVegan);
    }
        
    @Override
    public boolean isCandy(){return true;}
}