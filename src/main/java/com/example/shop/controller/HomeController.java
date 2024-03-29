package com.example.shop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.shop.model.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop.repository.ArticleRepository;

@CrossOrigin(origins = "https://localhost:8081")
@Controller
public class HomeController {
    
    // Try template
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getArticles(Model model) {
        return "index";
    }

    // @RequestMapping(value = "/iceSelection", method = RequestMethod.GET)
    // public ResponseEntity<List<Article>> getAllArticles(){
    //     List<Article> articles = new ArrayList<>();
    //     for (Article a : articleRepository.findAll()){
    //         articles.add(a);
    //     }
        

    //     if (articles.isEmpty()){
    //         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //     }

    //     return new ResponseEntity<>(articles, HttpStatus.OK);
    // }

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
