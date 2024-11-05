package com.example.shop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.shop.service.UserService.UserRecord;

import org.springframework.web.bind.annotation.PathVariable;

import com.example.shop.service.UserService;


@CrossOrigin(origins = "https://localhost:3000")
@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    

    @GetMapping("/api/v2/users")
    public ResponseEntity<List<UserRecord>> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/api/v2/users/{id}")
    public ResponseEntity<UserRecord> getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }
    
    
}
