package com.example.shop.controller;

import java.util.List;

import com.example.shop.model.Flavour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    //Just a redirect if flavour gets added
    @PostMapping(value = "/ice/addFlavour", produces = "text/plain")
    public ResponseEntity<String> addFlavour(@ModelAttribute Flavour flavour, Model model) {
        // verify flavour is not empty
        if (flavour.getDescription() == "" || flavour.getName() == ""){
            return ResponseEntity.badRequest().body("Request body is empty");
        };

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

    @GetMapping(value = "/flavours")
    public String getFlavours(Model model) {
        model.addAttribute("flavours", flavourRepository.findAll());
        return "ice :: ice(flavours=${flavours})";
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
    @PostMapping(value = "/ice/deleteFlavour/{id}")
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
