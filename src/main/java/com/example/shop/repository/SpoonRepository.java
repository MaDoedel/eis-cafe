package com.example.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.shop.model.Spoon;

public interface SpoonRepository extends JpaRepository<Spoon, Long> {
}
