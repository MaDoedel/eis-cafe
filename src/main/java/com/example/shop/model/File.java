package com.example.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    @NotBlank(message = "Invalid filename")
    private String fileName;

    @Column(name = "url")
    @NotBlank(message = "Invalid url")  
    private String url;

    @Column(name = "type")
    @NotBlank(message = "Invalid type")  
    private String type;

    public File(){}

    public File(String fileName, String url, String type){
        this.fileName = fileName; 
        this.url = url;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}