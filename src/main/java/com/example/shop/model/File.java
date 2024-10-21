package com.example.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "filename", length = 32)
    @NotBlank(message = "Invalid filename")
    private String fileName;

    @Column(name = "url", length = 255)
    @NotBlank(message = "Invalid url")  
    private String url;

    @Column(name = "type", length = 32)
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