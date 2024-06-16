package com.example.shop.controller;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.example.shop.service.CustomAuthentificationHandler;
import com.example.shop.service.CustomUserDetailsService;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity
public class SecurityConfiguarion {

    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(authenticationProvider);
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll() // if not, somehow login is denied
                .requestMatchers("/","/style.css", "/index.js", "/favicon.ico", "/image.png", "/images/**", "/jobs/apply").permitAll()
                .requestMatchers("/ice/**", "/jobs/reject/**", "/jobs/accept/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(login -> login 
                .loginPage("/login")
                .passwordParameter("password")
                .usernameParameter("username")
                .permitAll()
                .failureHandler(authenticationFailureHandler())
                .defaultSuccessUrl("/profile")
                )
            .logout(logout -> logout.permitAll())
            .exceptionHandling(eh -> eh.accessDeniedPage("/403"))
            .csrf(csrf -> csrf.disable());            
         
        return http.build();
    }


    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthentificationHandler();
    }
}