package com.example.shop.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;

import com.example.shop.model.Candy;
import com.example.shop.model.Cup;
import com.example.shop.model.Flavour;
import com.example.shop.model.Fruit;
import com.example.shop.model.Pricing;
import com.example.shop.model.Sauce;
import com.example.shop.model.Topping;
import com.example.shop.patterns.CandyFactory;
import com.example.shop.patterns.FruitFactory;
import com.example.shop.patterns.SauceFactory;

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
import com.example.shop.repository.ToppingRepository;

@CrossOrigin(origins = "https://localhost:8081")
@Controller
public class IceController {

    static final Path flavourFolder = Path.of("src", "main", "resources", "static", "images", "flavours");
    static final Path cupFolder = Path.of("src", "main", "resources", "static", "images", "cup");

        
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

    @Autowired
    ToppingRepository toppingRepository;
        

    @PostMapping(value = "/ice/addFruit", produces = "text/plain")
    public ResponseEntity<String> addFruit(
        @ModelAttribute Fruit fruit, 
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        FruitFactory ff = new FruitFactory(pricingRepository);
                    
        // Does path exist 
        // TODO make this final
        // if (!Files.exists(flavourFolder)) {
        //     Files.createDirectories(flavourFolder);
        // }

        // // Check if there is any image
        // if (imageFile == null){
        //     return ResponseEntity.badRequest().body("No image.");
        // }
        // if (!imageFile.getContentType().startsWith("image/")) {
        //     return ResponseEntity.badRequest().body("Uploaded file is not an image.");
        // }

        // verify flavour is not already in the list
        // note: technically multiple users could add the same flavour at the same time,
        //       consider a lock for the database to prevent this
        List<Topping> toppings = toppingRepository.findAll();
        for(Topping t : toppings){
            if(t.getName().equals(fruit.getName())){
                return ResponseEntity.badRequest().body("Fruit already exists");
            }
        }

        // save in persistent storage
        // String fileName;
        try {
            Topping nfruit = ff.createTopping(fruit.getName(), fruit.getDescription(), false);
            toppingRepository.save(nfruit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving Fruit");
        }

        // //safe image
        // Path filePath = flavourFolder.resolve(fileName);
        // Files.write(filePath, imageFile.getBytes());
        
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/ice/addSauce", produces = "text/plain")
    public ResponseEntity<String> addSauce(
        @ModelAttribute Sauce sauce, 
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        SauceFactory ff = new SauceFactory(pricingRepository);
                    
        // Does path exist 
        // TODO make this final
        // if (!Files.exists(flavourFolder)) {
        //     Files.createDirectories(flavourFolder);
        // }

        // // Check if there is any image
        // if (imageFile == null){
        //     return ResponseEntity.badRequest().body("No image.");
        // }
        // if (!imageFile.getContentType().startsWith("image/")) {
        //     return ResponseEntity.badRequest().body("Uploaded file is not an image.");
        // }

        // verify flavour is not already in the list
        // note: technically multiple users could add the same flavour at the same time,
        //       consider a lock for the database to prevent this
        List<Topping> toppings = toppingRepository.findAll();
        for(Topping t : toppings){
            if(t.getName().equals(sauce.getName())){
                return ResponseEntity.badRequest().body("Fruit already exists");
            }
        }

        // save in persistent storage
        // String fileName;
        try {
            Topping nSauce = ff.createTopping(sauce.getName(), sauce.getDescription(), sauce.getIsVegan());
            toppingRepository.save(nSauce);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving Fruit");
        }

        // //safe image
        // Path filePath = flavourFolder.resolve(fileName);
        // Files.write(filePath, imageFile.getBytes());
        
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/ice/addCandy", produces = "text/plain")
    public ResponseEntity<String> addCandy(
        @ModelAttribute Candy candy, 
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        CandyFactory ff = new CandyFactory(pricingRepository);
                    
        // Does path exist 
        // TODO make this final
        // if (!Files.exists(flavourFolder)) {
        //     Files.createDirectories(flavourFolder);
        // }

        // // Check if there is any image
        // if (imageFile == null){
        //     return ResponseEntity.badRequest().body("No image.");
        // }
        // if (!imageFile.getContentType().startsWith("image/")) {
        //     return ResponseEntity.badRequest().body("Uploaded file is not an image.");
        // }

        // verify flavour is not already in the list
        // note: technically multiple users could add the same flavour at the same time,
        //       consider a lock for the database to prevent this
        List<Topping> toppings = toppingRepository.findAll();
        for(Topping t : toppings){
            if(t.getName().equals(candy.getName())){
                return ResponseEntity.badRequest().body("Fruit already exists");
            }
        }

        // save in persistent storage
        // String fileName;
        try {
            Topping nCandy = ff.createTopping(candy.getName(), candy.getDescription(), candy.getIsVegan());
            toppingRepository.save(nCandy);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving Fruit");
        }

        // //safe image
        // Path filePath = flavourFolder.resolve(fileName);
        // Files.write(filePath, imageFile.getBytes());
        
        return ResponseEntity.ok().body(null);
    }
       
    
    @PostMapping(value = "/ice/addFlavour", produces = "text/plain")
    public ResponseEntity<String> addFlavour(
        @ModelAttribute Flavour flavour, 
        @RequestParam("image") MultipartFile imageFile) throws IOException {
                    
        // Does path exist 
        // TODO make this final
        if (!Files.exists(flavourFolder)) {
            Files.createDirectories(flavourFolder);
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
        Path filePath = flavourFolder.resolve(fileName);
        Files.write(filePath, imageFile.getBytes());
        
        return ResponseEntity.ok().body(null);
    }
    
    @DeleteMapping(value = "/ice/deleteFlavour/{id}")
    public ResponseEntity<String> deleteFlavour(@PathVariable("id") long id) throws IOException{
        try {
            flavourRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error deleting flavour");
        }

        if (!Files.exists(flavourFolder)) {
            Files.createDirectories(flavourFolder);
        }

        for (File file : flavourFolder.toFile().listFiles()){
            if (file.getName().contains(Long.toString(id))) {
                file.delete();
            }
        }

        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping(value = "/ice/deleteTopping/{id}")
    public ResponseEntity<String> deleteTopping(@PathVariable("id") long id) throws IOException{
        try {
            toppingRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error deleting topping");
        }
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping(value = "/ice/deleteCup/{id}")
    public ResponseEntity<String> deleteCup(@PathVariable("id") long id) throws IOException{
        try {
            cupRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error deleting topping");
        }
        return ResponseEntity.ok().body(null);
    }
    
    @PostMapping("/ice/addCup")
    public ResponseEntity<String> submitFlavors(
        @RequestParam java.util.Map<String, String> params,
        @RequestParam("CupName") String name,
        @RequestParam("CupPrice") BigDecimal price
        ) {

        Pattern fpattern = Pattern.compile("^(flavour)\\[(\\d+)\\]$");
        Pattern tpattern = Pattern.compile("^(topping)\\[(\\d+)\\]$");

        Cup cup = new Cup(name, price);
        List<Flavour> cupFlavours = new ArrayList<Flavour>(); 
        params.forEach((key, value) -> {
            Matcher matcher = fpattern.matcher(key);
            if (matcher.matches()) {
                long id = Long.parseLong(matcher.group(2));
                for (int i = 0; i < Integer.parseInt(value); i++){
                    cupFlavours.add(flavourRepository.findById(id).get());
                }
            }
        });
        cup.setFlavours(cupFlavours);
        
        List<Topping> cupToppings = new ArrayList<Topping>(); 
        params.forEach((key, value) -> {
            Matcher matcher = tpattern.matcher(key);
            if (matcher.matches()) {
                long id = Long.parseLong(matcher.group(2));
                for (int i = 0; i < Integer.parseInt(value); i++){
                    cupToppings.add(toppingRepository.findById(id).get());
                }
            }
        });
        cup.setToppings(cupToppings);


        try{
            cup.calculateVegan();
            cupRepository.save(cup);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Bad Cup");
        }

        return ResponseEntity.ok().body(null);
    }
}
    