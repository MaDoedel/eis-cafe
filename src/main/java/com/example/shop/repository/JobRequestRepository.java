package com.example.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop.model.JobRequest;

public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {
}
