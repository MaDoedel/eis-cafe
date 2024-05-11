package com.example.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop.model.Topping;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
}
