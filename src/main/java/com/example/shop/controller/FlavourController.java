package com.example.shop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.shop.model.Article;
import com.example.shop.model.Flavour;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.repository.ArticleRepository;
import com.example.shop.repository.FlavourRepository;


@CrossOrigin(origins = "https://localhost:8081")
@Controller
public class FlavourController {

    @Autowired
    ArticleRepository articleRepository; 

    @Autowired
    FlavourRepository flavourRepository; 

    // Try template
    @RequestMapping(value = "/iceSelection", method = RequestMethod.GET)
    public String getArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("flavour", new Flavour());
        return "iceSelection";
    }

    @RequestMapping(value = "/iceSelection/addFlavour", method = RequestMethod.POST)
    public String addFlavour(@ModelAttribute("flavour") Flavour flavour, Model model) {
        for(Flavour f : flavourRepository.findAll()){
            if(f.getName().equals(flavour.getName())){
                return "redirect:/iceSelection";
            }
        }

        if (flavour.getDescription() == "" || flavour.getName() == "" ){
            return "redirect:/iceSelection";
        }

        flavourRepository.save(flavour);
        return "redirect:/iceSelection";
    }

    @RequestMapping(value = "/iceSelection/deleteFlavour/{id}", method = RequestMethod.POST)
    public String updateArticleById(@PathVariable("id") long id){
        flavourRepository.deleteById(id);
        return "redirect:/iceSelection";
    }

}