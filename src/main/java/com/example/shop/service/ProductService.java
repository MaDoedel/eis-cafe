package com.example.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.model.Flavour;
import com.example.shop.model.Fruit;
import com.example.shop.model.Sauce;
import com.example.shop.model.Topping;
import com.example.shop.model.File;
import com.example.shop.model.Cup;
import com.example.shop.model.Candy;

import com.example.shop.repository.FlavourRepository;
import com.example.shop.repository.ToppingRepository;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;



import jakarta.validation.OverridesAttribute;

import com.example.shop.repository.CupRepository;

import java.io.IOException;

import com.example.shop.repository.FileRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ProductService implements ProductVisitor {

    static final Path flavourFolder = Path.of("src", "main", "resources", "static", "images", "flavours");
    static final Path cupFolder = Path.of("src", "main", "resources", "static", "images", "cups");
    static final Path toppingsFruitsFolder = Path.of("src", "main", "resources", "static", "images", "toppings", "fruits");
    static final Path toppingsCandiesFolder = Path.of("src", "main", "resources", "static", "images", "toppings", "candies");
    static final Path toppingsSauceFolder = Path.of("src", "main", "resources", "static", "images", "toppings", "sauce");

    @Autowired
    ToppingRepository toppingRepository;

    @Autowired
    CupRepository cupRepository;

    @Autowired
    FlavourRepository flavourRepository; 

    @Autowired
    FileRepository fileRepository;
    
    public MediaType getMediaType (String fileformat) {
        MediaType mediaType = null;
        if (fileformat.equals("jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (fileformat.equals("png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (fileformat.equals("gif")) {
            mediaType = MediaType.IMAGE_GIF;
        } else {
            return null;
        }
        return mediaType;
    }

    public ResponseEntity<Resource> getFlavourImage(Long id) {
        try{
            Flavour flavour = flavourRepository.findById(id).get();

            Path filePath = Paths.get(flavour.getFile().getUrl()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            MediaType mediaType = getMediaType(flavour.getFile().getType());
            if (mediaType == null) {
                System.out.println("mediaType is null");
                return ResponseEntity.badRequest().body(null);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + flavour.getFile().getFileName());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    public ResponseEntity<Resource> getToppingImage(Long id) {
        try{
            Topping topping = toppingRepository.findById(id).get();

            Path filePath = Paths.get(topping.getFile().getUrl()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            MediaType mediaType = getMediaType(topping.getFile().getType());
            if (mediaType == null) {
                return ResponseEntity.badRequest().body(null);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + topping.getFile().getFileName());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    public String formatChecker(MultipartFile imageFile) {
        Pattern pattern = Pattern.compile("image/(.*)");
        Matcher matcher = pattern.matcher(imageFile.getContentType());
        String fileformat = null;

        try{
            if (!matcher.find()) {
                return fileformat;
            }
            fileformat = matcher.group(1);
        } catch (Exception e){
            e.printStackTrace();
            return fileformat;        
        }

        return fileformat;
    }

    public ResponseEntity<String> saveFile(Path filePath, MultipartFile imageFile) throws IOException {
        try {
            Files.write(filePath, imageFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{ \"message\": \"Error saving image\" }");
        }

        return ResponseEntity.ok().body("");
    }



    @Override
    public ResponseEntity<String> addProduct(Flavour element, MultipartFile imageFile) throws IOException {
        String fileformat = formatChecker(imageFile);

        if (fileformat == null) {
            return ResponseEntity.ok().body("{\"message\": \"Image Error\"}"); 
        }

        try {
            element = flavourRepository.save(element);
        } catch (DataAccessException e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Error saving flavour\" }");
        }
        
        String fileName = element.getId() + "." + fileformat;
        Path filePath = flavourFolder.resolve(fileName);

        ResponseEntity<String> response = saveFile(filePath, imageFile);
        if (response.getStatusCode().is4xxClientError()) {
            return response;
        }

        File file = new File(fileName, filePath.toString(), fileformat);
        fileRepository.save(file);

        element.setFile(file);
        flavourRepository.save(element); 

        return ResponseEntity.ok().body("{ \"message\": \"Flavour added\" }");
    }

    @Override
    public ResponseEntity<String> deleteProduct(Flavour flavour) throws IOException {
        Long id = flavour.getId();

        try {
            if (!flavourRepository.existsById(id)) {
                return ResponseEntity.badRequest().body("{ \"message\": \"Flavour with id " + id + " does not exist\" }");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"ID is null\" }");
        }

        for(Cup c : cupRepository.findAll()){
            if (c.getFlavours().contains(flavourRepository.findById(id).get())){
                return ResponseEntity.badRequest().body("{ \"message\": \"Flavour with id " + flavour.getId() + " is used in a cup " + c.getName() + "\" }");
            }
        }

        if(flavourFolder.resolve(flavourRepository.findById(flavour.getId()).get().getFile().getFileName()).toFile().exists()){
            flavourFolder.resolve(flavourRepository.findById(flavour.getId()).get().getFile().getFileName()).toFile().delete();
        }

        flavourRepository.deleteById(flavour.getId());
        return ResponseEntity.ok().body("{ \"message\": \"Flavour with id " + flavour.getId() + " deleted successfully\" }");
    }

    // TODO - think about id change and how this effects the picture
    // TODO - think about image change

    @Override
    public ResponseEntity<String> editProduct(Flavour element, Long id) throws IOException {
        if (!flavourRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Flavour with id " + id + " does not exist\" }");
        }

        Flavour flavour = flavourRepository.findById(id).get();
        flavour.setName(element.getName());
        flavour.setDescription(element.getDescription());
        flavour.setIsVegan(element.getIsVegan());
        flavour.setPricing(element.getPricing());

        try {
            flavourRepository.save(flavour);
        } catch (DataAccessException e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Error saving flavour\" }");
        }

        return ResponseEntity.ok().body("{ \"message\": \"Flavour with id " + id + " edited successfully\" }");
    }

    // TODO - think about params and image
    @Override
    public ResponseEntity<String> addProduct(Cup element, MultipartFile imageFile) throws IOException {
        String fileformat = formatChecker(imageFile);

        if (fileformat == null) {
            return ResponseEntity.ok().body("{\"message\": \"Image Error\"}"); 
        }

        try {
            element = cupRepository.save(element);
        } catch (DataAccessException e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Error saving flavour\" }");
        }


        // Pattern fpattern = Pattern.compile("^(flavour)\\[(\\d+)\\]$");
        // Pattern tpattern = Pattern.compile("^(topping)\\[(\\d+)\\]$");

        // List<Flavour> cupFlavours = new ArrayList<Flavour>(); 
        // params.forEach((key, value) -> {
        //     Matcher matcher = fpattern.matcher(key);
        //     if (matcher.matches()) {
        //         long id = Long.parseLong(matcher.group(2));
        //         for (int i = 0; i < Integer.parseInt(value); i++){
        //             cupFlavours.add(flavourRepository.findById(id).get());
        //         }
        //     }
        // });
        // cup.setFlavours(cupFlavours);
        
        // List<Topping> cupToppings = new ArrayList<Topping>(); 
        // params.forEach((key, value) -> {
        //     Matcher matcher = tpattern.matcher(key);
        //     if (matcher.matches()) {
        //         long id = Long.parseLong(matcher.group(2));
        //         for (int i = 0; i < Integer.parseInt(value); i++){
        //             cupToppings.add(toppingRepository.findById(id).get());
        //         }
        //     }
        // });
        // cup.setToppings(cupToppings);

        try {
            element.calculateVegan();
            element = cupRepository.save(element);
        } catch (DataAccessException e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Error saving cup\" }");
        }
        
        String fileName = element.getId() + "." + fileformat;
        Path filePath = cupFolder.resolve(fileName);

        ResponseEntity<String> response = saveFile(filePath, imageFile);
        if (response.getStatusCode().is4xxClientError()) {
            return response;
        }

        File file = new File(fileName, filePath.toString(), fileformat);
        fileRepository.save(file);

        element.setFile(file);
        cupRepository.save(element);

        return ResponseEntity.ok().body("{ \"message\": \"Cup added\" }");
    }

    @Override 
    public ResponseEntity<String> deleteProduct(Cup cup) throws IOException {
        Long id = cup.getId();

        try {
            if (!cupRepository.existsById(id)) {
                return ResponseEntity.badRequest().body("{ \"message\": \"Cup with id " + id + " does not exist\" }");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"ID is null\" }");
        }

        if(cupFolder.resolve(cupRepository.findById(cup.getId()).get().getFile().getFileName()).toFile().exists()){
            cupFolder.resolve(cupRepository.findById(cup.getId()).get().getFile().getFileName()).toFile().delete();
        }

        cupRepository.deleteById(cup.getId());
        return ResponseEntity.ok().body("{ \"message\": \"Cup with id " + cup.getId() + " deleted successfully\" }");
    }

    // TODO - think about params and image
    @Override
    public ResponseEntity<String> editProduct(Cup element, Long id) throws IOException {
        if (!cupRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Cup with id " + id + " does not exist\" }");
        }

        Cup cup = cupRepository.findById(id).get();
        cup.setName(element.getName());
        cup.setPrice(element.getPrice());
        cup.setDescription(element.getDescription());
        cup.setFlavours(element.getFlavours());
        cup.setToppings(element.getToppings());

        try {
            cup.calculateVegan();
            cupRepository.save(cup);
        } catch (DataAccessException e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Error saving cup\" }");
        }

        return ResponseEntity.ok().body("{ \"message\": \"Cup with id " + id + " edited successfully\" }");
    }

    @Override
    public ResponseEntity<String> addProduct(Topping element, MultipartFile imageFile) throws IOException {
        String fileformat = formatChecker(imageFile);

        if (fileformat == null) {
            return ResponseEntity.ok().body("{\"message\": \"Image Error\"}"); 
        }

        try {
            element = toppingRepository.save(element);
        } catch (DataAccessException e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Error saving Topping\" }");
        }


        File file = null;
        if (element.isFruit()) {
            String fileName = element.getId() + "." + fileformat;
            Path filePath = toppingsFruitsFolder.resolve(fileName);

            ResponseEntity<String> response = saveFile(filePath, imageFile);
            if (response.getStatusCode().is4xxClientError()) {
                return response;
            }

            file = new File(fileName, filePath.toString(), fileformat);
            fileRepository.save(file);
        }

        if (element.isCandy()) {
            String fileName = element.getId() + "." + fileformat;
            Path filePath = toppingsCandiesFolder.resolve(fileName);

            ResponseEntity<String> response = saveFile(filePath, imageFile);
            if (response.getStatusCode().is4xxClientError()) {
                return response;
            }

            file = new File(fileName, filePath.toString(), fileformat);
            fileRepository.save(file);
        }

        if (element.isSauce()) {
            String fileName = element.getId() + "." + fileformat;
            Path filePath = toppingsSauceFolder.resolve(fileName);

            ResponseEntity<String> response = saveFile(filePath, imageFile);
            if (response.getStatusCode().is4xxClientError()) {
                return response;
            }

            file = new File(fileName, filePath.toString(), fileformat);
            fileRepository.save(file);
        }

        element.setFile(file);
        toppingRepository.save(element); 

        return ResponseEntity.ok().body("{ \"message\": \"Topping added\" }");
    }

    @Override
    public ResponseEntity<String> deleteProduct(Topping element) throws IOException{
        Long id = element.getId();

        for(Cup c : cupRepository.findAll()){
            if (c.getToppings().contains(toppingRepository.findById(id).get())){
                return ResponseEntity.badRequest().body("Topping with id " + id + " is used in a cup " + c.getName());
            }
        }
        
        if (element.getFile() != null) {
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
        }
        
        toppingRepository.deleteById(id);
        return ResponseEntity.ok().body("{ \"message\": \"Topping with id " + id + " deleted successfully\" }");
    }

    // TODO - think about image
    @Override 
    public ResponseEntity<String> editProduct(Topping element, Long id) throws IOException {
        if (!toppingRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Fruit with id " + id + " does not exist\" }");
        }

        Topping topping = toppingRepository.findById(id).get();
        Topping current = null;

        if (element.isCandy()) {
            Candy candy = (Candy) element;
            current = (Candy) topping;
            current.setName(candy.getName());
            current.setDescription(candy.getDescription());
            current.setIsVegan(candy.getIsVegan());
            current.setPricing(candy.getPricing());
        }

        if (element.isFruit()) {
            Fruit fruit = (Fruit) element;
            current = (Fruit) topping;
            current.setName(fruit.getName());
            current.setDescription(fruit.getDescription());
            current.setIsVegan(fruit.getIsVegan());
            current.setPricing(fruit.getPricing());
        }

        if (element.isSauce()) {
            Sauce sauce = (Sauce) element;
            current = (Sauce) topping;
            current.setName(sauce.getName());
            current.setDescription(sauce.getDescription());
            current.setIsVegan(sauce.getIsVegan());
            current.setPricing(sauce.getPricing());
        }

        if (current == null) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Error saving topping\" }");
        }

        try {
            toppingRepository.save(current);
        } catch (DataAccessException e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"Error saving topping\" }");
        }

        return ResponseEntity.ok().body("{ \"message\": \"Topping with id " + id + " edited successfully\" }");
    }


}
