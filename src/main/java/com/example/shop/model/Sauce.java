package com.example.shop.model;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;
import java.util.Objects;


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