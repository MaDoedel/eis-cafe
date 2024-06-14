package com.example.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	private final AuthenticationManager authenticationManager;

	public LoginController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
		Authentication authenticationRequest =
			UsernamePasswordAuthenticationToken.unauthenticated(username, password);
		Authentication authenticationResponse =
			this.authenticationManager.authenticate(authenticationRequest);

        if (authenticationResponse.isAuthenticated()) {
            return ResponseEntity.ok().body("Login successful");
        } else {
            return ResponseEntity.badRequest().body("Login failed");
        }
	}
}