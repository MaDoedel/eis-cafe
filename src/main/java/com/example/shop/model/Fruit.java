package com.example.shop.model;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;
import java.util.Objects;


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