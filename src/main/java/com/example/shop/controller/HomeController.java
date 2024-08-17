package com.example.shop.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.shop.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.shop.repository.CupRepository;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.FlavourRepository;
import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.repository.PricingRepository;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.ToppingRepository;



@CrossOrigin(origins = "https://localhost:8081")
@Controller
public class HomeController {

    @Autowired
    PasswordEncoder passwordEncoder;

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

    @Autowired
    PricingRepository pricingRepository;

    @Autowired
    FileRepository fileRepository;
    
    @GetMapping(value = "/")
    public String getAllLists(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername()).get(0);
            model.addAttribute("user", user);
        }

        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("toppings", toppingRepository.findAll());
        model.addAttribute("cups", cupRepository.findAll());
        model.addAttribute("jobRequests", jobRequestRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("prices", pricingRepository.findAll());
        model.addAttribute("files", fileRepository.findAll());


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
        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("toppings", toppingRepository.findAll());
        model.addAttribute("cups", cupRepository.findAll());
        model.addAttribute("jobRequests", jobRequestRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("prices", pricingRepository.findAll());
        model.addAttribute("files", fileRepository.findAll());

        return "profile :: profile";
    }
    
    @GetMapping(value = "/ice")
    public String getFlavours(Model model) {
        model.addAttribute("flavours", flavourRepository.findAll());
        model.addAttribute("toppings", toppingRepository.findAll());
        model.addAttribute("cups", cupRepository.findAll());


        return "ice :: ice(flavours=${flavours}, toppings=${toppings}, cups=${cups})";
    }

    @GetMapping(value = "/login")
    public String getLogin() {
        return "index";
    }


    @GetMapping(value = "/jobs")
    public String getJobs(Model model) {
        model.addAttribute("jobRequests", jobRequestRepository.findAll());
        return "jobs :: jobs(jobRequests=${jobRequests})";
    }

    
}
