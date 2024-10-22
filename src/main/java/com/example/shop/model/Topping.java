package com.example.shop.model;

import org.springframework.web.multipart.MultipartFile;

import com.example.shop.service.ProductVisitor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.example.shop.service.Element;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "topping_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Topping  implements Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, length = 32)
    @NotBlank(message = "Need some name")
    private String name;

    @Column(name = "description", length = 255)
    @NotBlank(message = "description")
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="price_id")
    private Pricing pricing;

    @Column(name = "isVegan")
    private boolean isVegan;

    @JsonIgnore
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

    @Override
    public void accept(ProductVisitor visitor, MultipartFile file) throws IOException {
        visitor.addProduct(this, file);
    }

    @Override
    public void accept(ProductVisitor visitor, Long id) throws IOException {
        visitor.editProduct(this, id);
    }

    @Override
    public void accept(ProductVisitor visitor) throws IOException {
        visitor.deleteProduct(this);
    }

}
