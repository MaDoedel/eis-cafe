package com.example.shop.model;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;



@Entity
@Table(name = "flavour")
public class Flavour {

    @Id 
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "name", unique=true)
    @NotBlank(message = "Invalid name")
    private String name; 

    @Column(name = "isVegan")
    @NotNull(message = "Invalid value")
    private boolean isVegan; 

    @Column(name = "description")
    @NotBlank(message = "Invalid description")
    private String description; 

    @ManyToOne
    @JoinColumn(name="price_id")
    private Pricing pricing;
 
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private File file;


    public Flavour(){}

    public Flavour(String name, boolean isVegan, String description){
        this.name = name; 
        this.isVegan = isVegan; 
        this.description = description;
    }

    public File getFile(){
        return file;
    }
    public void setFile(File file){
        this.file = file;
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

    public void setIsVegan(boolean isVegan) {
        this.isVegan = isVegan;
    }

    public boolean getIsVegan() {
        return isVegan;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPricing(Pricing p) {
        this.pricing = p;
    }

    public Pricing getPricing() {
        return pricing;
    }
}