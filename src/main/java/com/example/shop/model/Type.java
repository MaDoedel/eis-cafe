package com.example.shop.model;
import jakarta.persistence.*;

@Entity
@Table(name = "type")
public class Type {

    @Id 
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "name")
    private String name; 

    public Type(){}

    public Type(String name){
        this.name = name; 
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
}