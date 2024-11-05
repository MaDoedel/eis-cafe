package com.example.shop.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.shop.service.CustomAuthentificationHandler;
import com.example.shop.service.CustomUserDetailsService;
import com.example.shop.service.CustomAccessDeniedHandler;
import com.example.shop.service.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguarion {

    private CustomUserDetailsService customUserDetailsService;
    private JwtRequestFilter jwtRequestFilter;

    public SecurityConfiguarion(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(authenticationProvider);
	}

    // @Bean
    // public InMemoryUserDetailsManager userDetailsService() {
    //     UserDetails user = User.withUsername("admin")
    //         .password(passwordEncoder().encode("secret"))
    //         .roles("ADMIN")
    //         .build();
    //     return new InMemoryUserDetailsManager(user);
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/v2/ice/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/v2/ice/**").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v2/ice/**").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v2/ice/**").hasAnyAuthority("ROLE_ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/v2/users").hasAnyAuthority("ROLE_ADMIN") // Gets all users
                .requestMatchers(HttpMethod.GET, "/api/v2/users/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // Gets a specific user (himslef)

                .requestMatchers(HttpMethod.POST, "/api/v2/users").hasAnyAuthority("ROLE_ADMIN") // Creating a User is an admin thing mostly
                .requestMatchers(HttpMethod.DELETE, "/api/v2/users/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // deleting yourself or deleting another user is should be possible for anyone
                .requestMatchers(HttpMethod.PUT, "/api/v2/users/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // deleting yourself or deleting another user is should be possible for anyone

                .requestMatchers(HttpMethod.GET, "/api/v2/jobs").hasAnyAuthority("ROLE_ADMIN") // See all Jobrequests
                .requestMatchers(HttpMethod.POST, "/api/v2/jobs").permitAll() // Create a Jobrequest should be possible for everyone
                .requestMatchers(HttpMethod.DELETE, "/api/v2/jobs/**").hasAnyAuthority("ROLE_ADMIN") // Delete a Jobrequest should be an admin thing


                .requestMatchers("/","/style.css", "/index.js", "/favicon.ico", "/image.png", "/images/**", "/jobs/apply").permitAll()
                .requestMatchers("/ice/**", "/jobs/reject/**", "/jobs/accept/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll())
            .exceptionHandling(eh -> eh
                .authenticationEntryPoint(getAuthenticationEntryPoint())
                .accessDeniedHandler(getAccessDeniedHandler()))
            .csrf(csrf -> csrf
                .disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthentificationHandler();
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}