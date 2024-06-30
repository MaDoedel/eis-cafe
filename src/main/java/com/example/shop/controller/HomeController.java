package com.example.shop.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;

import com.example.shop.model.Cup;
import com.example.shop.model.Flavour;
import com.example.shop.model.Pricing;
import com.example.shop.model.User;

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
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.ToppingRepository;

import org.springframework.security.crypto.password.PasswordEncoder;



@CrossOrigin(origins = "https://localhost:8081")
@Controller
public class HomeController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ArticleRepository articleRepository; 

    @Autowired
    FlavourRepository flavourRepository; 

    @Autowired
    JobRequestRepository jobRequestRepository; 

    @Autowired
    CupRepository cupRepository; 

    @Autowired
    ToppingRepository toppingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    
    @GetMapping(value = "/")
    public String getAllLists(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername()).get(0);
            model.addAttribute("user", user);
        }

        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("toppings", toppingRepository.findAll());
        model.addAttribute("cups", cupRepository.findAll());
        model.addAttribute("jobRequests", jobRequestRepository.findAll());

        // User user = new User("Matteo", "Anedda", "matteo.aneddama@gmail.com", "active");
        // user.setRoles(roleRepository.findByName("ROLE_ADMIN"));
        // user.setPassword(passwordEncoder.encode("password"));
        // userRepository.save(user);

        // System.out.println("-----------------");
        // for (User u : userRepository.findAll()) {
        //     System.out.println(u.getEmail());
        //     for (com.example.shop.model.Role r : u.getRoles()) {
        //         System.out.println(r.getName());
        //     }
        // }

        return "index";
    }

    @GetMapping(value = "/profile")
    public String profil(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        // Get user without overriding the CustomUserDetailsService loadUserByUsername
        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername()).get(0);
            model.addAttribute("user", user);
        }
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("toppings", toppingRepository.findAll());
        model.addAttribute("cups", cupRepository.findAll());
        model.addAttribute("jobRequests", jobRequestRepository.findAll());
        return "me :: me";
    }
    
    @GetMapping(value = "/ice")
    public String getFlavours(Model model) {
        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("toppings", toppingRepository.findAll());
        model.addAttribute("cups", cupRepository.findAll());


        return "ice :: ice(flavours=${flavours}, articles=${articles}, toppings=${toppings}, cups=${cups})";
    }

    @GetMapping(value = "/login")
    public String getLogin() {
        return "login :: login";
    }


    @GetMapping(value = "/jobs")
    public String getJobs(Model model) {
        model.addAttribute("jobRequests", jobRequestRepository.findAll());
        return "jobs :: jobs(jobRequests=${jobRequests})";
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
