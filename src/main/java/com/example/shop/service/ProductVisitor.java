package com.example.shop.service;

import com.example.shop.model.Flavour;
import com.example.shop.model.Topping;
import com.example.shop.model.Cup;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface ProductVisitor {
    
    ResponseEntity<String> addProduct(Flavour element, MultipartFile file) throws IOException;
    ResponseEntity<String> editProduct(Flavour element, Long id) throws IOException;
    ResponseEntity<String> deleteProduct(Flavour element) throws IOException;

    ResponseEntity<String> addProduct(Topping element, MultipartFile file) throws IOException ;
    ResponseEntity<String> editProduct(Topping element, Long id) throws IOException ;
    ResponseEntity<String> deleteProduct(Topping element) throws IOException ;

    ResponseEntity<String> addProduct(Cup element, MultipartFile file) throws IOException;
    ResponseEntity<String> editProduct(Cup element, Long id) throws IOException ;
    ResponseEntity<String> deleteProduct(Cup element) throws IOException ;
}
