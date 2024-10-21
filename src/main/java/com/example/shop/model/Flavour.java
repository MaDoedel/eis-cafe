package com.example.shop.model;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import com.example.shop.service.Element;
import com.example.shop.service.ProductVisitor;


@Entity
@Table(name = "flavour")
public class Flavour implements Element {

    @Id 
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "name", unique=true, length = 32)
    @NotBlank(message = "Invalid name")
    private String name; 

    @Column(name = "isVegan")
    @NotNull(message = "Invalid value")
    private boolean isVegan; 

    @Column(name = "description", length = 255)
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