package com.example.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.service.CustomUserDetailsService;
import com.example.shop.service.JWTService;

@Controller
public class LoginController {

	private AuthenticationManager authenticationManager;
    private JWTService jwtservice;
    private CustomUserDetailsService userDetailsService;

    public LoginController(AuthenticationManager authenticationManager, JWTService jwtservice, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtservice = jwtservice;
        this.userDetailsService = userDetailsService;
    }
	
	public record AuthenticationResponse(String jwt) {}

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
        @RequestParam("username") String username, 
		@RequestParam("password") String password) throws Exception {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password)
			);

			final UserDetails userDetails = userDetailsService
					.loadUserByUsername(username);

			final String jwt = jwtservice.generateToken(userDetails);

			return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


}