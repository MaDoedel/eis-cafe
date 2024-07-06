package com.example.shop.repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop.model.JobRequest;

public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {

    @Query("SELECT j FROM JobRequest j JOIN j.user u WHERE u.email = ?1")
    List<JobRequest> findByEmail(String email);
}
