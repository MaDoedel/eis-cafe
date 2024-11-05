package com.example.shop.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;

@Service
public class UserService {
    
    private UserRepository userRepository;


    public record UserRecord (String name, String surname, String email) {}

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<UserRecord>> getUsers() {
        List<UserRecord> users = userRepository.findAll().stream()
                .map(user -> new UserRecord(user.getName(), user.getSurname(), user.getEmail()))
                .toList(); 

        return ResponseEntity.ok().body(users);
    }

    public ResponseEntity<UserRecord> getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        UserRecord userRecord = new UserRecord(user.getName(), user.getSurname(), user.getEmail());
        return ResponseEntity.ok().body(userRecord);
    }
}
