package com.example.shop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.model.File;
import com.example.shop.model.User;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.UserRepository;

import ch.qos.logback.core.model.Model;

@Controller
public class JobsController {

    @Autowired
    UserRepository userRepository; 

    @Autowired
    FileRepository fileRepository; 

    @PostMapping(value = "/jobs/apply", produces = "text/plain")
    public ResponseEntity<String> addUser(@ModelAttribute User user, @RequestParam("CV") MultipartFile pdfFile) throws IOException {
        
        // Does path exist 
        // TODO make this final
        Path destinationFolder = Path.of("src", "main", "resources", "static", "user");
        if (!Files.exists(destinationFolder)) {
            Files.createDirectories(destinationFolder);
        }

        // Check if there is any image
        if (pdfFile == null){
            return ResponseEntity.badRequest().body("No CV.");
        }
        if (!pdfFile.getContentType().startsWith("application/pdf")) {
            return ResponseEntity.badRequest().body("Uploaded file is not an pdf.");
        }


        // save in persistent storage
        String fileName;
        try {
            System.out.println(user.getEmail());
            user = userRepository.save(user);
            fileName = user.getId() + pdfFile.getOriginalFilename().substring(pdfFile.getOriginalFilename().lastIndexOf('.'));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong");
        }

        //safe image
        Path filePath = destinationFolder.resolve(fileName);
        Files.write(filePath, pdfFile.getBytes());

        File file = new File(fileName, filePath.toString(), true);
        file.setUser(user);
        fileRepository.save(file);
        
        return ResponseEntity.ok().body(null);
    }
}