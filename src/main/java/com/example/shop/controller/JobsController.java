package com.example.shop.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.service.JobService;
import com.example.shop.service.JobService.UserRecord;
import com.example.shop.service.JobService.JobRequestRecord;


import jakarta.transaction.Transactional;

@CrossOrigin(origins = "https://localhost:3000")
@Controller
public class JobsController {

    static final Path urdir = Path.of("src", "main", "resources", "static", "user");
    JobService jobService;

    public JobsController(JobService jobService) {
        this.jobService = jobService;
    }

    @Transactional
    @PostMapping(value = "/api/v2/jobs", produces = "text/plain")
    public ResponseEntity<String> processJobRequests(
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("surname") String surname,
        @RequestParam("comment") String comment, 
        @RequestParam("jobDescription") String jobDescription,
        @RequestParam(value = "cv") MultipartFile pdfFile) throws IOException {
            UserRecord userRecord = new UserRecord(name, surname, email, comment, jobDescription);
            return jobService.processJobRequest(userRecord, pdfFile);
    }

    @GetMapping(value = "/api/v2/jobs")
    public ResponseEntity<List<JobRequestRecord>> getJobRequests() {
        return jobService.getJobRequests();
    }

    @GetMapping(value = "/api/v2/jobs/{id}")
    public ResponseEntity<Resource> getFile(
        @PathVariable("id") long requestId) throws IOException {
        return jobService.getFile(requestId);
    }

    @DeleteMapping(value = "/api/v2/jobs/{id}/{decision}")
    public ResponseEntity<String> decideJobrequest(
        @PathVariable("id") long requestId, 
        @PathVariable("decision") String decision) throws IOException {
        return jobService.decideJobrequest(requestId, decision);
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