package com.example.shop.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.shop.model.File;
import com.example.shop.model.JobRequest;
import com.example.shop.model.User;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.UserRepository;

@Service
public class JobService {
    
    static final Path urdir = Path.of("src", "main", "resources", "static", "user");

    JobRequestRepository jobRequestRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    FileRepository fileRepository;

    public JobService(
        JobRequestRepository jobRequestRepository, 
        RoleRepository roleRepository, 
        PasswordEncoder passwordEncoder, 
        UserRepository userRepository,
        FileRepository fileRepository) {
            this.jobRequestRepository = jobRequestRepository;
            this.roleRepository = roleRepository;
            this.passwordEncoder = passwordEncoder;
            this.userRepository = userRepository;
            this.fileRepository = fileRepository;
    }


    public record UserRequestRecord(String name, String surname, String email, String comment, String applicantType) {}
    public ResponseEntity<String> processJobRequest(
        UserRequestRecord Request,
        @RequestParam(value = "CV") MultipartFile pdfFile) throws IOException {
            User user = new User(Request.name(), Request.surname(), Request.email(), "pending");

            user.setRoles(roleRepository.findByName("ROLE_NONE"));
            user.setPassword(passwordEncoder.encode("none")); // Do we need that... maybe for dashboard reasons?

            if (pdfFile == null || pdfFile.isEmpty()) {
                return ResponseEntity.badRequest().body("{ \"message\" : \"No file uploaded.\" }");
            }   

            if (!pdfFile.getContentType().startsWith("application/pdf")) {
                return ResponseEntity.badRequest().body("{ \"message\" : \"Uploaded file is not a pdf.\" }");
            }
            // save in persistent storage
            String fileName;

            // Unique email somehow doesn't work
            if (userRepository.findByEmail(user.getEmail()).size() > 0) {
                return ResponseEntity.badRequest().body("{ \"message\" : \"Email already in use.\" }");
            }
            
            user = userRepository.save(user);
            fileName = user.getId() + ".pdf";
           
            //safe image
            Path filePath = urdir.resolve(fileName);
            Files.write(filePath, pdfFile.getBytes());

            File file = new File(fileName, filePath.toString(), "pdf");
            fileRepository.save(file);

            JobRequest jobRequest = new JobRequest(user, file, ((Request.comment() == null) || (Request.comment().length() == 0)) ? "" : Request.comment(), Request.applicantType());
            jobRequestRepository.save(jobRequest);
            
            return ResponseEntity.ok().body("{ \"message\" : \"Successfull Request\" }");
    }

    public record JobRequestRecord(Long jobRequestId , String name, String surname, String email, String applicantType) {}
    public ResponseEntity<List<JobRequestRecord>> getJobRequests(){
        List<JobRequestRecord> jobRequests = new ArrayList<>();

        jobRequestRepository.findAll().forEach(jobRequest -> {
            User user = jobRequest.getUser();
            jobRequests.add(new JobRequestRecord(jobRequest.getId(), user.getName(), user.getSurname(), user.getEmail(), jobRequest.getJobDescription()));
        });
        
        return ResponseEntity.ok().body(jobRequests);
    }

    public ResponseEntity<Resource> getFile(long id) throws IOException {
       try{
            Path filePath = Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + jobRequestRepository.findById(id).get().getFile().getFileName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    public ResponseEntity<String> decideJobrequest(long id, String decision) {

        if (jobRequestRepository.findById(id).isEmpty()) {
            return ResponseEntity.badRequest().body("{ \"message\" : \"Invalid request\" }");
        }

        if (decision.equals("accept")) {
            if (Files.exists(Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize())) {
                Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize().toFile().delete();
            }
            User user = jobRequestRepository.findById(id).get().getUser();
            user.setState("accepted");    
            userRepository.save(user);

            jobRequestRepository.deleteById(id);
            return ResponseEntity.ok().body("{ \"message\" : \"Request accepted\" }");
        }

        if (decision.equals("reject")) {
            if (Files.exists(Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize())) {
                Paths.get(jobRequestRepository.findById(id).get().getFile().getUrl()).normalize().toFile().delete();
            }
            Long user_id = jobRequestRepository.findById(id).get().getUser().getId();
            jobRequestRepository.deleteById(id);
            userRepository.deleteById(user_id);
            return ResponseEntity.ok().body("{ \"message\" : \"Request rejected\" }");
        }
        return ResponseEntity.badRequest().body("{ \"message\" : \"Error\" }");
    }
}
