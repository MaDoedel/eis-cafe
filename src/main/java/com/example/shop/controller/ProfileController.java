package com.example.shop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.UserRepository;

@Controller
public class ProfileController {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private UserRepository userRepository;

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
    
}
