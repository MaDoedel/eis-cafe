package com.example.shop.model;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;
import java.util.Objects;


@Entity
@DiscriminatorValue("Candy")
public class Candy extends Topping {

    public Candy() {}

    public Candy(String name, String description, boolean isVegan) {
        super(name, description, isVegan);
    }
        
}