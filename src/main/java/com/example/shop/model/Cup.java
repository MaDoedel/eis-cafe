package com.example.shop.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.*;
import com.example.shop.service.Element;
import com.example.shop.service.ProductVisitor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;

@Entity
@Table(name ="cup")
public class Cup  implements Element {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id; 

    @Column(name = "name", length = 32)
    String name;

    @Column(name = "description", length = 255)
    String description;

    @Column(name = "isVegan")
    boolean isVegan = false;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "cup_flavours",
        joinColumns = @JoinColumn(name = "cup_id"),
        inverseJoinColumns = @JoinColumn(name = "flavour_id")
    )
    private List<Flavour> flavours;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "cup_toppings",
        joinColumns = @JoinColumn(name = "cup_id"),
        inverseJoinColumns = @JoinColumn(name = "topping_id")
    )
    private List<Topping> toppings;

    @Column(name = "price")
    BigDecimal price;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id", referencedColumnName = "id")
    private File file;


    public Cup() {
    }

    public Cup(String name, BigDecimal bd, String desc) {
        this.name = name; 
        this.price = bd;
        this.description = desc;
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

    public void setFlavours(List<Flavour> flav) {
        this.flavours = flav;
    }

    public List<Flavour> getFlavours() {
        return this.flavours;
    }
    
    public List<Topping> getToppings() {
        return this.toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal bd) {
        this.price = bd;
    }

    public void calculateVegan(){
        for (Flavour f : flavours) {
            if (!f.getIsVegan()) {this.isVegan = false; return;}
        }

        for (Topping t : toppings) {
            if (!t.getIsVegan()) {this.isVegan = false; return;}
        }
        this.isVegan = true;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String desc) {
        this.description = desc;
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
