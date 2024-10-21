package com.example.shop.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface Element {
    // add
    void accept(ProductVisitor visitor,  MultipartFile file) throws IOException;
    // edit
    void accept(ProductVisitor visitor, Long id) throws IOException;
    // delete
    void accept(ProductVisitor visitor) throws IOException;
}
