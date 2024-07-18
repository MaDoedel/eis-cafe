package com.example.shop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.mock.web.MockMultipartFile;

import com.example.shop.model.Candy;
import com.example.shop.model.Cup;
import com.example.shop.model.File;
import com.example.shop.model.Flavour;
import com.example.shop.model.JobRequest;
import com.example.shop.model.Role;
import com.example.shop.model.Sauce;
import com.example.shop.model.User;
import com.example.shop.model.Topping;
import com.example.shop.model.Fruit;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.CupRepository;
import com.example.shop.repository.FlavourRepository;
import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.PricingRepository;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.ToppingRepository;
import com.example.shop.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JobsControllerTest {
        

    @Autowired
    private JobRequestRepository jrRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;    

    @BeforeEach
    public void setUp() throws Exception{
        try{
            User user = new User("Matteo", "Anedda", "Email", "State");
            user.setPassword("some String");
            user.setRoles(roleRepository.findByName("ROLE_NONE"));
            userRepository.save(user);
        } catch (Exception e) { }
    }

	@AfterEach
	public void tearDown() throws Exception{
        try {
            JobRequest jr = jrRepository.findByEmail("john.doe@example.com").get(0);
            
            if (Files.exists(Paths.get(jr.getFile().getUrl()).normalize())) {
                Paths.get(jr.getFile().getUrl()).normalize().toFile().delete();
            }

            long user_id = jr.getUser().getId();
            jrRepository.deleteById(jr.getId());
            userRepository.deleteById(user_id);
        } catch (Exception e) {}

        try {
            User user = userRepository.findByEmail("Email").get(0);
            userRepository.delete(user);
        } catch (Exception e) {}
	}


    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testSuccessfulSubmission() throws Exception {
        MockMultipartFile cvFile = new MockMultipartFile("CV", "cv.pdf", "application/pdf", "PDF content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/jobs/apply")
                .file(cvFile)
                .param("name", "John")
                .param("surname", "Doe")
                .param("email", "john.doe@example.com")
                .param("comment", "Looking forward to this opportunity")
                .param("applicantType", "Full-time"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Shoudn't go through because of the missing CV and html accept statement
    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testMissingCV() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/jobs/apply")
                .param("name", "John")
                .param("surname", "Doe")
                .param("email", "john.doe@example.com")
                .param("comment", "Looking forward to this opportunity")
                .param("applicantType", "Full-time"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testInvalidFileType() throws Exception {
        MockMultipartFile invalidFile = new MockMultipartFile("CV", "cv.txt", "text/plain", "Not a PDF content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/jobs/apply")
                .file(invalidFile)
                .param("name", "John")
                .param("surname", "Doe")
                .param("email", "john.doe@example.com")
                .param("comment", "Looking forward to this opportunity")
                .param("applicantType", "Full-time"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testRequestSameUser() throws Exception {
        MockMultipartFile cvFile = new MockMultipartFile("CV", "cv.pdf", "application/pdf", "PDF content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/jobs/apply")
                .file(cvFile)
                .param("name", "Matteo")
                .param("surname", "Anedda")
                .param("email", "Email")
                .param("comment", "Looking forward to this opportunity")
                .param("applicantType", "Full-time"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // @Test
    // @WithMockUser(username ="admin", roles={"ADMIN"})
    // public void testUserSaveError() throws Exception {
    //     // Simulate user repository error
    //     // This would typically involve mocking the userRepository to throw an exception

    //     MockMultipartFile cvFile = new MockMultipartFile("CV", "cv.pdf", "application/pdf", "PDF content".getBytes());

    //     mockMvc.perform(MockMvcRequestBuilders.multipart("/jobs/apply")
    //             .file(cvFile)
    //             .param("name", "John")
    //             .param("surname", "Doe")
    //             .param("email", "john.doe@example.com")
    //             .param("comment", "Looking forward to this opportunity")
    //             .param("applicantType", "Full-time"))
    //             .andExpect(MockMvcResultMatchers.status().isBadRequest());
    // }
}
