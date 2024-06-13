package com.example.shop.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.shop.model.Role;
import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;

import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) {
        List<User> users = userRepository.findByEmail(mail);

        if (users.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(users.get(0).getEmail(), users.get(0).getPassword(), mapRolesToAuthorities(users.get(0).getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection <Role> roles) {
        Collection<? extends GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return authorities;
    }
}
