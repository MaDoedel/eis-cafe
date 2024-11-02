package com.example.shop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shop.model.Cup;
import com.example.shop.model.File;
import com.example.shop.model.Flavour;
import com.example.shop.model.Fruit;
import com.example.shop.model.Role;
import com.example.shop.model.Topping;
import com.example.shop.repository.CupRepository;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.FlavourRepository;
import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.ToppingRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ProfileController {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    @Autowired
    private FlavourRepository flavourRepository;

    @Autowired
    private CupRepository cupRepository;


    @GetMapping("/download/cv/{id}")
    public ResponseEntity<Resource> downloadCV(@PathVariable("id") long id) throws IOException {
        try{
            Path filePath = Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
    
    
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + jobRequestRepository.findById(id).get().getFile().getFileName());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
        
    }

    @DeleteMapping("/jobs/reject/{id}")
    public ResponseEntity<String> rejectRequest(@PathVariable("id") long id) throws IOException {

        try{
            // A request may have a associated CV, which is cascaded, but deleting the entry does not effect any file. Lets delete it here 
            if (Files.exists(Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize())) {
                Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize().toFile().delete();
            }

            Long user_id = jobRequestRepository.findById(id).get().getUser().getId();
            // Now delete the request, prior to the user
            // The file is cascaded, so its deleted once its request is gone
            jobRequestRepository.deleteById(id);
            userRepository.deleteById(user_id);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Wrong ID: " + id);
        }

        return ResponseEntity.ok().body("");
    }

    @DeleteMapping("/profile/deleteFile/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable("id") long id) throws IOException {

        for (Topping topping : toppingRepository.findAll()) {
            if (topping.getFile().getId() == fileRepository.findById(id).get().getId()) {
                return ResponseEntity.badRequest().body("File is in use by a topping");
            }
        }

        for (Flavour flavour : flavourRepository.findAll()) {
            if (flavour.getName().equals("Sahne")) {continue;}
            
            if (flavour.getFile().getId() == fileRepository.findById(id).get().getId()) {
                return ResponseEntity.badRequest().body("File is in use by a flavour");
            }
        }

        for (Cup cup : cupRepository.findAll()) {
            if (cup.getFile().getId() == fileRepository.findById(id).get().getId()) {
                return ResponseEntity.badRequest().body("File is in use by a cup");
            }
        }

        try{

            if (Files.exists(Paths.get(fileRepository.findById(id).get().getUrl()).normalize())) {
                Paths.get(fileRepository.findById(id).get().getUrl()).normalize().toFile().delete();
            }

            fileRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Wrong ID: " + id);
        }

        return ResponseEntity.ok().body("");
    }

    @DeleteMapping("/jobs/accept/{id}")
    public ResponseEntity<String> acceptRequest(@PathVariable("id") long id) throws IOException {

        try{
            // lets delete the CV? Why not
            // A request may have a associated CV, which is cascaded, but deleting the entry does not effect any file. Lets delete it here 
            if (Files.exists(Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize())) {
                Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize().toFile().delete();
            }
            
            // Change the User State
            jobRequestRepository.findById(id).get().getUser().setState("accepted");

            // Write an EMail, banzaiii

            // Now delete the request
            jobRequestRepository.deleteById(id);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Wrong ID: " + id );
        }

        return ResponseEntity.ok().body("");
    }

    @PostMapping("profile/addRole")
    public ResponseEntity<String> addRole(@ModelAttribute Role role) {
        
        Pattern pattern = Pattern.compile("ROLE_(.*)");
        Matcher matcher = pattern.matcher(role.getName());

        if (matcher.find()) {
            try {
                roleRepository.save(role);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("Role already exists");
            }
        } else {
            return ResponseEntity.badRequest().body("Role name must start with ROLE_");
        }
        
        return ResponseEntity.ok().body("New Role added");
    }
    
    
}
