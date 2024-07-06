package com.example.shop.repository;

import java.util.List;
import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Flavour;


public interface FlavourRepository extends JpaRepository<Flavour, Long> {

    @Query("SELECT f FROM Flavour f WHERE f.name = ?1")
    List<Flavour> findByName(String name);
}

