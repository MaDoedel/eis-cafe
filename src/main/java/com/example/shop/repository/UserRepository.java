package com.example.shop.repository;

import java.util.List;
import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
}

