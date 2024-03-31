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
public class HomeController {

    @Autowired
    ArticleRepository articleRepository; 

    @Autowired
    FlavourRepository flavourRepository; 
    
    // Try template
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getAllLists(Model model) {
        // Get the lists
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("flavour", new Flavour());

        return "index";
    }

    //Just a redirect if flavour gets added
    @RequestMapping(value = "/ice/addFlavour", method = RequestMethod.POST)
    public String addFlavour(@ModelAttribute("flavour") Flavour flavour) {
        System.out.println("something");
        for(Flavour f : flavourRepository.findAll()){
            if(f.getName().equals(flavour.getName())){
                return "ice :: ice";
            }
        }

        if (flavour.getDescription() == "" || flavour.getName() == "" ){
            return "ice :: ice";
        }

        flavourRepository.save(flavour);
        return "ice :: ice";
    }
    
    /* 
    Returns catchable Response HTML OK addFlavour
    @RequestMapping(value = "/ice/addFlavour", method = RequestMethod.POST)
    public ResponseEntity<String> addFlavour(@ModelAttribute("flavour") Flavour flavour) {
        System.out.println("something");
        for(Flavour f : flavourRepository.findAll()){
            if(f.getName().equals(flavour.getName())){
                return ResponseEntity.badRequest().body("");
            }
        }

        if (flavour.getDescription() == "" || flavour.getName() == "" ){
            return ResponseEntity.badRequest().body("");
        }

        flavourRepository.save(flavour);
        return ResponseEntity.ok().body("");
    }
    */

    //Just a redirect if flavour gets delete
    @RequestMapping(value = "/ice/deleteFlavour/{id}", method = RequestMethod.POST)
    public String deleteFlavour(@PathVariable("id") long id){
        flavourRepository.deleteById(id);
        return "redirect:/index";
    }
    
    /* 
    Returns catchable Response HTML OK deleteFlavour
    @RequestMapping(value = "/ice/deleteFlavour/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFlavour(@PathVariable("id") long id){
        flavourRepository.deleteById(id);
        return ResponseEntity.ok().body("");
    }
    */

    // @PostMapping("/articles")
    // public ResponseEntity<Article> getArticle(@RequestBody Article article){
    //     try {
    //         Article _Article = articleRepository.save(new Article(article.getName(), article.getBrand(), article.getDescription()));
    //         return new ResponseEntity<>(_Article, HttpStatus.CREATED);
    //     } catch (Exception e){
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    // @GetMapping("/articles/{id}")
    // public ResponseEntity<Article> getArticleById(@PathVariable("id") long id){
    //     Optional<Article> a = articleRepository.findById(id);

    //     if (!a.isPresent()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);}

    //     return new ResponseEntity<>(a.get(), HttpStatus.OK);
    // }

    // @PutMapping("/articles/{id}")
    // public ResponseEntity<Article> updateArticleById(@PathVariable("id") long id, @RequestBody Article article){
    //     Optional<Article> a = articleRepository.findById(id);
    //     if (!a.isPresent()) { return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
    //     a.get().setName(article.getName());
    //     a.get().setDescription(article.getDescription());
    //     a.get().setBrand(article.getBrand());
    //     return new ResponseEntity<>(articleRepository.save(a.get()), HttpStatus.OK);
    // }

    // @DeleteMapping("/articles")
    // public ResponseEntity<Article> deleteAllArticles(){
    //     articleRepository.deleteAll();
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }

    // @DeleteMapping("/articles/{id}")
    // public ResponseEntity<Article> deleteArticleById(@PathVariable("id") long id){
    //     Optional<Article> a = articleRepository.findById(id);

    //     if(!a.isPresent()) {return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);}
    //     articleRepository.deleteById(id);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }

}
