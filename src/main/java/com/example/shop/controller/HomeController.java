package com.example.shop.controller;

import java.util.List;

import com.example.shop.model.Flavour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.shop.repository.ArticleRepository;
import com.example.shop.repository.FlavourRepository;

@CrossOrigin(origins = "https://localhost:8081")
@Controller
public class HomeController {

    @Autowired
    ArticleRepository articleRepository; 

    @Autowired
    FlavourRepository flavourRepository; 
    
    @GetMapping(value = "/")
    public String getAllLists(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("flavours", flavourRepository.findAll());
        return "index";
    }

    @PostMapping(value = "/ice/addFlavour", produces = "text/plain")
    public ResponseEntity<String> addFlavour(@ModelAttribute Flavour flavour, Model model) {
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
        try {
            flavour = flavourRepository.save(flavour);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving flavour");
        }

        // pass the updated list to the view
        flavours.add(flavour);
        
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
    

    @DeleteMapping(value = "/ice/deleteFlavour/{id}")
    public ResponseEntity<String> deleteFlavour(@PathVariable("id") long id){
        try {
            flavourRepository.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting flavour");
        }
        return ResponseEntity.ok().body(null);
    }
}
