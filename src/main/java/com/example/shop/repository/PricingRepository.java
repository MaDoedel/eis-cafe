package com.example.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shop.model.Pricing;

public interface PricingRepository extends JpaRepository<Pricing, Long> {
        
        @Query("SELECT p FROM Pricing p WHERE p.description = ?1")
        List<Pricing> findByDescripton(String name);
}
