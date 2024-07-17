package com.example.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shop.model.Cup;

public interface CupRepository extends JpaRepository<Cup, Long> {
    @Query("SELECT c FROM Cup c WHERE c.name = ?1")
    List<Cup> findByName(String name);
}