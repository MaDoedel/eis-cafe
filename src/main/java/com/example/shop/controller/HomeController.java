package com.example.shop.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;

import com.example.shop.model.Cup;
import com.example.shop.model.Flavour;
import com.example.shop.model.Pricing;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.repository.ArticleRepository;
import com.example.shop.repository.CupRepository;
import com.example.shop.repository.FlavourRepository;
import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.repository.PricingRepository;


@CrossOrigin(origins = "https://localhost:8081")
@Controller
public class HomeController {

    @Autowired
    ArticleRepository articleRepository; 

    @Autowired
    FlavourRepository flavourRepository; 

    @Autowired
    UserRepository userRepository; 

    @Autowired
    JobRequestRepository jobRequestRepository; 

    @Autowired
    CupRepository cupRepository; 

    @Autowired
    PricingRepository pricingRepository;
    
    @GetMapping(value = "/")
    public String getAllLists(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("jobRequests", jobRequestRepository.findAll());
        return "index";
    }

    @PostMapping(value = "/ice/addFlavour", produces = "text/plain")
    public ResponseEntity<String> addFlavour(@ModelAttribute Flavour flavour, @RequestParam("image") MultipartFile imageFile, Model model) throws IOException {
        
        // Does path exist 
        // TODO make this final
        Path destinationFolder = Path.of("src", "main", "resources", "static", "images", "flavours");
        if (!Files.exists(destinationFolder)) {
            Files.createDirectories(destinationFolder);
        }

        // Check if there is any image
        if (imageFile == null){
            return ResponseEntity.badRequest().body("No image.");
        }
        if (!imageFile.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body("Uploaded file is not an image.");
        }

        // verify flavour is not already in the list
        // note: technically multiple users could add the same flavour at the same time,
        //       consider a lock for the database to prevent this
        List<Flavour> flavours = flavourRepository.findAll();
        for(Flavour f : flavours){
            if(f.getName().equals(flavour.getName())){
                return ResponseEntity.badRequest().body("Flavour already exists");
            }
        }

        // save in persistent storage
        String fileName;
        try {
            for (Pricing p : pricingRepository.findAll()){
                if (p.getDescription().equals("Spoon")){
                    flavour.setPricing(p);
                }
            }
            flavour = flavourRepository.save(flavour);
            fileName = flavour.getId() + imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf('.'));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving flavour");
        }

        //safe image
        Path filePath = destinationFolder.resolve(fileName);
        Files.write(filePath, imageFile.getBytes());
        
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/login", produces = "text/plain")
    public ResponseEntity<String> login() {
        return ResponseEntity.badRequest().body(null);
    }
    
    @GetMapping(value = "/ice")
    public String getFlavours(Model model) {
        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        return "ice :: ice(flavours=${flavours}, articles=${articles})";
    }

    @GetMapping(value = "/jobs")
    public String getJobs(Model model) {
        model.addAttribute("jobRequests", jobRequestRepository.findAll());
        return "jobs :: jobs(jobRequests=${jobRequests})";
    }
    

    @DeleteMapping(value = "/ice/deleteFlavour/{id}")
    public ResponseEntity<String> deleteFlavour(@PathVariable("id") long id) throws IOException{
        try {
            flavourRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error deleting flavour");
        }

        Path destinationFolder = Path.of("src", "main", "resources", "static", "images", "flavours");
        if (!Files.exists(destinationFolder)) {
            Files.createDirectories(destinationFolder);
        }

        for (File file : destinationFolder.toFile().listFiles()){
            if (file.getName().contains(Long.toString(id))) {
                file.delete();
            }
        }

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/ice/addCup")
    public ResponseEntity<String> submitFlavors(@RequestParam  java.util.Map<String, String> quant) {
        
        quant.forEach((key, value) -> {
            System.out.println("Flavor ID: " + key + ", Quantity: " + value);
        });
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/download/cv/{id}")
    public ResponseEntity<Resource> downloadCV(@PathVariable("id") long id) throws IOException {

        Path filePath = Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize();
        Resource resource = new UrlResource(filePath.toUri());


        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + jobRequestRepository.findById(id).get().getFile().getFileName());
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
