package com.example.shop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.model.File;
import com.example.shop.model.JobRequest;
import com.example.shop.model.User;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.JobService;
import com.example.shop.service.JobService.UserRecord;
import com.example.shop.service.JobService.JobRequestRecord;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.transaction.Transactional;

@Controller
public class JobsController {

    static final Path urdir = Path.of("src", "main", "resources", "static", "user");

    @Autowired
    UserRepository userRepository; 

    @Autowired
    FileRepository fileRepository; 

    @Autowired
    JobRequestRepository jobRequestRepository; 

    @Autowired
    RoleRepository roleRepository; 

    @Autowired
    PasswordEncoder passwordEncoder;

    JobService jobService;

    public JobsController(JobService jobService) {
        this.jobService = jobService;
    }

    @Transactional
    @PostMapping(value = "/jobs", produces = "text/plain")
    public ResponseEntity<String> processJobRequests(
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("surname") String surname,
        @RequestParam("comment") String comment, 
        @RequestParam("applicantType") String applicantType,
        @RequestParam(value = "CV") MultipartFile pdfFile) throws IOException {
            UserRecord userRecord = new UserRecord(name, surname, email, comment, applicantType);
            return jobService.processJobRequest(userRecord, pdfFile);
    }

    @GetMapping(value = "/jobs")
    public ResponseEntity<List<JobRequestRecord>> getJobRequests() {
        return jobService.getJobRequests();
    }

    @GetMapping(value = "/jobs/{id}")
    public ResponseEntity<Resource> getFile(
        @PathVariable("id") long RequestId) throws IOException {
        return jobService.getFile(RequestId);
    }

    // Old code

    @Transactional
    @PostMapping(value = "/jobs/apply", produces = "text/plain")
    public ResponseEntity<String> addUser(
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("surname") String surname,
        @RequestParam("comment") String comment, 
        @RequestParam("applicantType") String applicantType,
        @RequestParam(value = "CV") MultipartFile pdfFile) throws IOException {
            UserRecord userRecord = new UserRecord(name, surname, email, comment, applicantType);
            return jobService.processJobRequest(userRecord, pdfFile);
    }
}