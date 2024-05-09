package com.example.shop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.model.File;
import com.example.shop.model.JobRequest;
import com.example.shop.model.User;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.UserRepository;

import ch.qos.logback.core.model.Model;

@Controller
public class JobsController {

    static final Path urdir = Path.of("src", "main", "resources", "static", "user");

    @Autowired
    UserRepository userRepository; 

    @Autowired
    FileRepository fileRepository; 

    @Autowired
    JobRequestRepository jobRequestRepository; 

    @PostMapping(value = "/jobs/apply", produces = "text/plain")
    public ResponseEntity<String> addUser(
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("surname") String surname,
        @RequestParam("comment") String comment, 
        @RequestParam("applicantType") String applicantType,
        @RequestParam(value = "CV") MultipartFile pdfFile) throws IOException {
            User user = new User(name, surname, email, "pending");

            if (!Files.exists(urdir)) {
                Files.createDirectories(urdir);
            }

            // Better check pdf again
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
            Path filePath = urdir.resolve(fileName);
            Files.write(filePath, pdfFile.getBytes());

            File file = new File(fileName, filePath.toString());
            fileRepository.save(file);

            JobRequest jobRequest = new JobRequest(user, file, ((comment == null) || (comment.length() == 0)) ? "" : comment, applicantType);
            jobRequestRepository.save(jobRequest);
            
            return ResponseEntity.ok().body(null);
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
            // The file is cascaded, so its deleted once is request is gone
            jobRequestRepository.deleteById(id);
            userRepository.deleteById(user_id);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong");
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
            return ResponseEntity.badRequest().body("Something went wrong");
        }

        return ResponseEntity.ok().body("");
    }
    
}