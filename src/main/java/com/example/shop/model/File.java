package com.example.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "isCV")
    @NotNull(message = "Invalid filename")
    private boolean isCV;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public File(){}

    public File(String fileName, String url, boolean isCV){
        this.fileName = fileName; 
        this.url = url;
        this.isCV = isCV; 
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


    public void setIsCV(boolean isCV) {
        this.isCV = isCV;
    }

    public boolean getIsCV() {
        return isCV;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}