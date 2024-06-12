// package com.example.shop.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.config.annotation.web.configuration.*;


// @Configuration
// //@EnableWebSecurity
// public class SecurityConfiguarion {
	
//     // @Bean
//     // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     //     http.authorizeHttpRequests(auth -> auth
//     //             .requestMatchers("/", "/index", "/register", "/style.css", "/index.js", "/favicon.ico", "/image.png", "/images/**", "/user/**", "/h2-ui/**").permitAll()
//     //             .anyRequest().authenticated())
//     //             .formLogin(config -> config.loginPage("/login").permitAll())
//     //             .logout(LogoutConfigurer::permitAll);
//     //     return http.build();
//     // }
// }