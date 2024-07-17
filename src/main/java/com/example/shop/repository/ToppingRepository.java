package com.example.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Topping;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
    @Query("SELECT t FROM Topping t WHERE t.name = ?1")
    List<Topping> findByName(String name);
}
