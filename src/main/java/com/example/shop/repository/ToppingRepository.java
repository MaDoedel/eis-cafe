package com.example.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Candy;
import com.example.shop.model.Fruit;
import com.example.shop.model.Sauce;
import com.example.shop.model.Topping;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
    @Query("SELECT t FROM Topping t WHERE t.name = ?1")
    List<Topping> findByName(String name);

    @Query("SELECT t FROM Topping t WHERE TYPE(t) = Fruit")
    List<Fruit> findAllFruits();

    @Query("SELECT t FROM Topping t WHERE TYPE(t) = Candy")
    List<Candy> findAllCandies();

    @Query("SELECT t FROM Topping t WHERE TYPE(t) = Sauce")
    List<Sauce> findAllSauce();

}
