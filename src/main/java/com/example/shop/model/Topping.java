package com.example.shop.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "topping_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Topping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    @NotBlank(message = "Need some name")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name="price_id")
    private Pricing pricing;

    @Column(name = "isVegan")
    private boolean isVegan;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private File file;


    public Topping() {
    }

    public Topping(String name, String description, boolean isVegan) {
        this.name = name;
        this.description = description;
        this.isVegan = isVegan;
    }
    
    public void setFile(File file){
        this.file = file;
    }

    public File getFile(){
        return file;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsVegan() {
        return this.isVegan;
    }

    public boolean getIsVegan() {
        return this.isVegan;
    }

    public void setIsVegan(boolean isVegan) {
        this.isVegan = isVegan;
    }

    public Pricing getPricing() {
        return this.pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    public boolean isFruit() {return false;}
    public boolean isSauce() {return false;}
    public boolean isCandy() {return false;}

}
