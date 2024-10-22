package com.example.shop.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.model.Candy;
import com.example.shop.model.Cup;
import com.example.shop.model.Flavour;
import com.example.shop.model.Fruit;
import com.example.shop.model.Sauce;
import com.example.shop.model.Topping;
import com.example.shop.repository.CupRepository;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.FlavourRepository;
import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.PricingRepository;
import com.example.shop.repository.ToppingRepository;
import com.example.shop.repository.UserRepository;

import com.example.shop.service.ProductService;


@CrossOrigin(origins = "https://localhost:3000")
@Controller
public class IceController {

    static final Path flavourFolder = Path.of("src", "main", "resources", "static", "images", "flavours");
    static final Path toppingsFruitsFolder = Path.of("src", "main", "resources", "static", "images", "toppings", "fruits");
    static final Path toppingsCandiesFolder = Path.of("src", "main", "resources", "static", "images", "toppings", "candies");
    static final Path toppingsSauceFolder = Path.of("src", "main", "resources", "static", "images", "toppings", "sauce");

    static final Path cupFolder = Path.of("src", "main", "resources", "static", "images", "cups");

    // Field injections bad, I know
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
        
    @Autowired
    FileRepository fileRepository;

    @Autowired
    ProductService productService;

    // v3 will be GraphQL

    @GetMapping(value = "/api/v2/ice/flavours")
    public ResponseEntity<List<Flavour>> getFlavours(){
        return ResponseEntity.ok().body(flavourRepository.findAll());
    }

    @PutMapping(value = "/api/v2/ice/flavours/{id}")
    public ResponseEntity<String> editFlavours(
        @PathVariable("id") Long id,
        @RequestParam("id") Long newId, 
        @RequestParam("name") String name,
        @RequestParam("vegan") boolean vegan,
        @RequestParam("description") String description,
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        Flavour flavour = new Flavour();
        flavour.setName(name);
        flavour.setIsVegan(vegan);
        flavour.setDescription(description);
        flavour.setPricing(pricingRepository.findByDescripton("Spoon").get(0));
        flavour.setFile(null);

        return productService.editProduct(flavour, id);
    }

    @DeleteMapping(value = "/api/v2/ice/flavours/{id}")
    public ResponseEntity<String> deleteFlavours(
        @PathVariable("id") Long id) throws IOException {
        return productService.deleteProduct(flavourRepository.findById(id).get());
    }

    @PostMapping(value = "/api/v2/ice/flavours")
    public ResponseEntity<String> addFlavours(
        @RequestParam("name") String name,
        @RequestParam("vegan") boolean vegan,
        @RequestParam("description") String description,
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        Flavour flavour = new Flavour();
        flavour.setName(name);
        flavour.setIsVegan(vegan);
        flavour.setDescription(description);
        flavour.setPricing(pricingRepository.findByDescripton("Spoon").get(0));
        flavour.setFile(null);

        return productService.addProduct(flavour, imageFile);
    }

    @GetMapping(value = "/api/v2/ice/cups")
    public ResponseEntity<List<Cup>> getCups(){
        return ResponseEntity.ok().body(cupRepository.findAll());
    }

    @PostMapping(value = "/api/v2/ice/cups")
    public ResponseEntity<String> addCups(
        @RequestParam java.util.Map<String, String> params,
        @RequestParam("CupName") String name,
        @RequestParam("CupPrice") BigDecimal price,
        @RequestParam("CupDescription") String description,
        @RequestParam(value= "image", required = true) MultipartFile imageFile ) throws IOException {
        
        Cup cup = new Cup();
        cup.setName(name);
        cup.setPrice(price);
        cup.setDescription(description);
        cup.setFlavours(new ArrayList<Flavour>());
        cup.setToppings(new ArrayList<Topping>());
        cup.setFile(null);

        return productService.addProduct(cup, imageFile);
    }

    @DeleteMapping(value = "/api/v2/ice/cups/{id}")
    public ResponseEntity<String> deleteCups(
        @PathVariable("id") Long id) throws IOException {
        return productService.deleteProduct(cupRepository.findById(id).get());
    }

    @PutMapping(value = "/api/v2/ice/cups/{id}")
    public ResponseEntity<String> editCups(
        @PathVariable("id") Long id,
        @RequestParam("id") Long newId, 
        @RequestParam("name") String name,
        @RequestParam("price") BigDecimal price,
        @RequestParam("description") String description,
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        Cup cup = new Cup();
        cup.setName(name);
        cup.setPrice(price);
        cup.setDescription(description);
        cup.setFlavours(new ArrayList<Flavour>());
        cup.setToppings(new ArrayList<Topping>());
        cup.setFile(null);

        return productService.editProduct(cup, id);
    }


    @GetMapping(value = "/api/v2/ice/toppings/{type}")
    public ResponseEntity<List<?>> getToppings(
        @PathVariable("type") String type){
            System.out.println(type);
        if (type.equals("fruits")){
            return ResponseEntity.ok().body(toppingRepository.findAllFruits());
        }
        if (type.equals("candy")){
            return ResponseEntity.ok().body(toppingRepository.findAllCandies());
        }
        if (type.equals("sauce")){
            return ResponseEntity.ok().body(toppingRepository.findAllSauce());
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping(value = "/api/v2/ice/toppings/{type}")
    public ResponseEntity<String> addToppings(
        @PathVariable("type") String type,
        @RequestParam("name") String name,
        @RequestParam("vegan") boolean vegan,
        @RequestParam("description") String description,
        @RequestParam("image") MultipartFile imageFile) throws IOException {
            System.out.println(type);

            Topping topping = null;
            if (type.equals("fruits")) {
                topping = new Fruit();
                topping.setName(name);
                topping.setIsVegan(vegan);
                topping.setDescription(description);
                topping.setPricing(pricingRepository.findByDescripton("Fruit").get(0));
                topping.setFile(null);
                return productService.addProduct(topping, imageFile);
            }

            if (type.equals("sauce")) {
                topping = new Sauce();
                topping.setName(name);
                topping.setIsVegan(vegan);
                topping.setDescription(description);
                topping.setPricing(pricingRepository.findByDescripton("Sauce").get(0));
                topping.setFile(null);
                return productService.addProduct(topping, imageFile);
            }

            if (type.equals("candy")) {
                topping = new Candy();
                topping.setName(name);
                topping.setIsVegan(vegan);
                topping.setDescription(description);
                topping.setPricing(pricingRepository.findByDescripton("Candy").get(0));
                topping.setFile(null);
                return productService.addProduct(topping, imageFile);
            }

            return productService.addProduct(topping, imageFile);
    }

    @DeleteMapping(value = "/api/v2/ice/toppings/{type}/{id}")
    public ResponseEntity<String> deleteToppings(
        @PathVariable("type") String type,
        @PathVariable("id") Long id) throws IOException {
        return productService.deleteProduct(toppingRepository.findById(id).get());
        }
    
    @PutMapping(value = "/api/v2/ice/toppings/{type}/{id}")
    public ResponseEntity<String> editTopping (
        @PathVariable("id") Long id,
        @PathVariable("type") String type,
        @RequestParam("id") Long newId, 
        @RequestParam("name") String name,
        @RequestParam("vegan") boolean vegan,
        @RequestParam("description") String description,
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        Topping topping = null;
        if (type.equals("fruit")) {
            topping = new Fruit();
            topping.setName(name);
            topping.setIsVegan(vegan);
            topping.setDescription(description);
            topping.setPricing(pricingRepository.findByDescripton("Fruit").get(0));
            topping.setFile(null);
            return productService.editProduct(topping, id);
        }

        if (type.equals("sauce")) {
            topping = new Sauce();
            topping.setName(name);
            topping.setIsVegan(vegan);
            topping.setDescription(description);
            topping.setPricing(pricingRepository.findByDescripton("Sauce").get(0));
            topping.setFile(null);
            return productService.editProduct(topping, id);
        }

        if (type.equals("candy")) {
            topping = new Candy();
            topping.setName(name);
            topping.setIsVegan(vegan);
            topping.setDescription(description);
            topping.setPricing(pricingRepository.findByDescripton("Candy").get(0));
            topping.setFile(null);
            return productService.editProduct(topping, id);
        }

        return productService.editProduct(topping, id);
    }

    @GetMapping(value = "/api/v2/ice/toppings/{type}/{id}/images")
    public ResponseEntity<Resource> editToppingImage(
        @PathVariable("type") String type,
        @PathVariable("id") Long id) throws IOException {
        return productService.getToppingImage(id);
    }

    @GetMapping(value = "/api/v2/ice/flavours/{id}/images")
    public ResponseEntity<Resource> editFlavourImage(
        @PathVariable("id") Long id) throws IOException {
        return productService.getFlavourImage(id);
    }



    // ------------------------------- Old code here -------------------------------

      
    @Transactional
    @PostMapping(value = "/ice/addFruit", produces = "text/plain")
    public ResponseEntity<String> addFruit(
        @ModelAttribute Fruit fruit, 
        @RequestParam("image") MultipartFile imageFile) throws IOException {
        
        Pattern pattern = Pattern.compile("image/(.*)");
        Matcher matcher = pattern.matcher(imageFile.getContentType());
        String fileformat = "";

        try{
            if (!matcher.find()) {
                return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
            }
            fileformat = matcher.group(1);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
        }

        String fileName;
        try {
            fruit.setPricing(pricingRepository.findByDescripton("Fruit").get(0));
            fruit.setFile(null);
            fruit = toppingRepository.save(fruit);
            fileName = fruit.getId() + "." + fileformat;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving topping");
        }

        //safe image
        Path filePath = toppingsFruitsFolder.resolve(fileName);
        Files.write(filePath, imageFile.getBytes());


        com.example.shop.model.File file = new com.example.shop.model.File(fileName, filePath.toString(), fileformat);
        fileRepository.save(file);

        fruit.setFile(file);
        toppingRepository.save(fruit);

        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/ice/addSauce", produces = "text/plain")
    public ResponseEntity<String> addSauce(
        @ModelAttribute Sauce sauce, 
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        Pattern pattern = Pattern.compile("image/(.*)");
        Matcher matcher = pattern.matcher(imageFile.getContentType());
        String fileformat = "";

        try{
            if (!matcher.find()) {
                return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
            }
            fileformat = matcher.group(1);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
        }

        String fileName;
        try {
            sauce.setPricing(pricingRepository.findByDescripton("Sauce").get(0));
            sauce.setFile(null);
            sauce = toppingRepository.save(sauce);
            fileName = sauce.getId() + "." + fileformat;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving topping");
        }

        //safe image
        Path filePath = toppingsFruitsFolder.resolve(fileName);
        Files.write(filePath, imageFile.getBytes());


        com.example.shop.model.File file = new com.example.shop.model.File(fileName, filePath.toString(), fileformat);
        fileRepository.save(file);

        sauce.setFile(file);
        toppingRepository.save(sauce);

        return ResponseEntity.ok().body(null); 
    }

    @PostMapping(value = "/ice/addCandy", produces = "text/plain")
    public ResponseEntity<String> addCandy(
        @ModelAttribute Candy candy, 
        @RequestParam("image") MultipartFile imageFile) throws IOException {

        Pattern pattern = Pattern.compile("image/(.*)");
        Matcher matcher = pattern.matcher(imageFile.getContentType());
        String fileformat = "";

        try{
            if (!matcher.find()) {
                return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
            }
            fileformat = matcher.group(1);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
        }

        String fileName;
        try {
            candy.setPricing(pricingRepository.findByDescripton("Candy").get(0));
            candy.setFile(null);
            candy = toppingRepository.save(candy);
            fileName = candy.getId() + "." + fileformat;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving topping");
        }

        //safe image
        Path filePath = toppingsFruitsFolder.resolve(fileName);
        Files.write(filePath, imageFile.getBytes());


        com.example.shop.model.File file = new com.example.shop.model.File(fileName, filePath.toString(), fileformat);
        fileRepository.save(file);

        candy.setFile(file);
        toppingRepository.save(candy);

        return ResponseEntity.ok().body(null);     
    }
       
    
    @Transactional
    @PostMapping(value = "/ice/addFlavour", produces = "text/plain")
    public ResponseEntity<String> addFlavour(
        @ModelAttribute Flavour flavour, 
        @RequestParam(value= "image", required = true) MultipartFile imageFile) throws IOException {
                    
        // Get the exact type
        Pattern pattern = Pattern.compile("image/(.*)");
        Matcher matcher = pattern.matcher(imageFile.getContentType());
        String fileformat = "";

        // looks bad
        try{
            if (!matcher.find()) {
                return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
            } 
            fileformat = matcher.group(1);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
        }

        // save in persistent storage
        String fileName;
        try {
            flavour.setPricing(pricingRepository.findByDescripton("Spoon").get(0));
            flavour.setFile(null); // Just in case you know
            flavour = flavourRepository.save(flavour);
            fileName = flavour.getId() + "." + fileformat;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving flavour");
        }

        //safe image
        Path filePath = flavourFolder.resolve(fileName);
        Files.write(filePath, imageFile.getBytes());


        com.example.shop.model.File file = new com.example.shop.model.File(fileName, filePath.toString(), fileformat);
        fileRepository.save(file);

        flavour.setFile(file);
        flavourRepository.save(flavour);

        return ResponseEntity.ok().body(null);
    }
    
    @Transactional
    @DeleteMapping(value = "/ice/deleteFlavour/{id}")
    public ResponseEntity<String> deleteFlavour(@PathVariable("id") long id) throws IOException{
        try {
            if (!flavourRepository.existsById(id)){
                return ResponseEntity.badRequest().body("Flavour with id " + id + " does not exist");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ID is null");
        }


        for(Cup c : cupRepository.findAll()){
            if (c.getFlavours().contains(flavourRepository.findById(id).get())){
                return ResponseEntity.badRequest().body("Flavour with id " + id + " is used in a cup " + c.getName());
            }
        }

        if(flavourFolder.resolve(flavourRepository.findById(id).get().getFile().getFileName()).toFile().exists()){
            flavourFolder.resolve(flavourRepository.findById(id).get().getFile().getFileName()).toFile().delete();
        }

        flavourRepository.deleteById(id);

        return ResponseEntity.ok().body("Flavour with id " + id + " deleted");
    }

    @Transactional
    @DeleteMapping(value = "/ice/deleteTopping/{id}")
    public ResponseEntity<String> deleteTopping(@PathVariable("id") long id) throws IOException{
        try {
            if (!toppingRepository.existsById(id)){
                return ResponseEntity.badRequest().body("Topping with id " + id + " does not exist");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ID is null");
        }

        for(Cup c : cupRepository.findAll()){
            if (c.getToppings().contains(toppingRepository.findById(id).get())){
                return ResponseEntity.badRequest().body("Topping with id " + id + " is used in a cup " + c.getName());
            }
        }


        // TODO: Refactor this
        if (toppingRepository.findById(id).get().isFruit()){
            if(toppingsFruitsFolder.resolve(toppingRepository.findById(id).get().getFile().getFileName()).toFile().exists()){
                toppingsFruitsFolder.resolve(toppingRepository.findById(id).get().getFile().getFileName()).toFile().delete();
            }
        }

        if (toppingRepository.findById(id).get().isCandy()){
            if(toppingsCandiesFolder.resolve(toppingRepository.findById(id).get().getFile().getFileName()).toFile().exists()){
                toppingsCandiesFolder.resolve(toppingRepository.findById(id).get().getFile().getFileName()).toFile().delete();
            }
        }

        if (toppingRepository.findById(id).get().isSauce()){
            if(toppingsSauceFolder.resolve(toppingRepository.findById(id).get().getFile().getFileName()).toFile().exists()){
                toppingsSauceFolder.resolve(toppingRepository.findById(id).get().getFile().getFileName()).toFile().delete();
            }
        }

        toppingRepository.deleteById(id);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping(value = "/ice/deleteCup/{id}")
    public ResponseEntity<String> deleteCup(@PathVariable("id") long id) throws IOException{
        try {
            if (!cupRepository.existsById(id)){
                return ResponseEntity.badRequest().body("Cup with id " + id + " does not exist");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ID is null");
        }
     
        if(cupFolder.resolve(cupRepository.findById(id).get().getFile().getFileName()).toFile().exists()){
            cupFolder.resolve(cupRepository.findById(id).get().getFile().getFileName()).toFile().delete();
        }
        
        cupRepository.deleteById(id);
        return ResponseEntity.ok().body(null);
    }
    
    @PostMapping("/ice/addCup")
    public ResponseEntity<String> addCup(
        @RequestParam java.util.Map<String, String> params,
        @RequestParam("CupName") String name,
        @RequestParam("CupPrice") BigDecimal price,
        @RequestParam("CupDescription") String description,
        @RequestParam(value= "image", required = true) MultipartFile imageFile
        ) throws IOException {

        // Get the exact type
        Pattern pattern = Pattern.compile("image/(.*)");
        Matcher imatcher = pattern.matcher(imageFile.getContentType());
        String fileformat = "";

        // looks bad
        try{
            if (!imatcher.find()) {
                return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
            } 
            fileformat = imatcher.group(1);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid type. It should be in the format 'image/...'");
        }


        Pattern fpattern = Pattern.compile("^(flavour)\\[(\\d+)\\]$");
        Pattern tpattern = Pattern.compile("^(topping)\\[(\\d+)\\]$");

        Cup cup = new Cup(name, price, description);
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

    
        String fileName;
        try{
            cup.calculateVegan();
            cup.setFile(null); // Just in case you know
            cup = cupRepository.save(cup);
            fileName = cup.getId() + "." + fileformat;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Bad Cup");
        }

         //safe image
         Path filePath = cupFolder.resolve(fileName);
         Files.write(filePath, imageFile.getBytes());
 
 
         com.example.shop.model.File file = new com.example.shop.model.File(fileName, filePath.toString(), fileformat);
         fileRepository.save(file);

         cup.setFile(file);
         cupRepository.save(cup);
 

        return ResponseEntity.ok().body(null);
    }
}
    