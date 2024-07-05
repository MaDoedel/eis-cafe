package com.example.shop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.shop.model.File;
import com.example.shop.model.JobRequest;
import com.example.shop.model.User;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.JobRequestRepository;
import com.example.shop.repository.UserRepository;

import jakarta.transaction.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfileControllerTest {

	// For this layer, a user should at least be authenticated to access the resources, so the tests are doubled

	@Autowired
    private JobRequestRepository jobRequestRepository;

	@Autowired
    private FileRepository fileRepository;

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	@WithMockUser(username="admin")
    public void setUp() {
		// Jesus lord, I need factories for that... 
		User bonnie = new User("Bonnie", "Elizabeth Parker", "Bonnie@example.com", "pending");
		bonnie.setPassword("admin");
		bonnie = userRepository.save(bonnie);
	
		User clyde = new User("Clyde", "Chestnut Barrow", "Clyde@example.com", "pending");
		clyde.setPassword("admin");
		clyde = userRepository.save(clyde);
	
		File bonnieCV = new File("BonnieCV", "src/test/resources/BonnieCV.pdf", "application/pdf");	
		File clydeCV = new File("ClydeCV", "src/test/resources/ClydeCV.pdf", "application/pdf");
	
		JobRequest jobRequestBonnie = new JobRequest(bonnie, bonnieCV, "pending", "miniJob");
		jobRequestBonnie = jobRequestRepository.save(jobRequestBonnie);
	
		JobRequest jobRequestClyde = new JobRequest(clyde, clydeCV, "pending", "miniJob");
		jobRequestClyde = jobRequestRepository.save(jobRequestClyde);
    }

	@AfterEach
	public void tearDown() {

		jobRequestRepository.findAll().forEach(jobRequest -> {
			if (Files.exists(Paths.get(jobRequest.getFile().getUrl()).normalize())) {
                Paths.get(jobRequest.getFile().getUrl()).normalize().toFile().delete();
            }
            Long user_id = jobRequest.getUser().getId();
            jobRequestRepository.delete(jobRequest);
            userRepository.deleteById(user_id);
		});

		userRepository.findAll().forEach(User -> {
			if (User.getName().equals("Bonnie") || User.getName().equals("Clyde")) {
				userRepository.delete(User);
			}
		});

		if (fileRepository.count() > 0) {
			fileRepository.deleteAll();
		}
	}

	
	@Test
	void testUnauthorizedDownload() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/download/cv/1"))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
	}

	@Test
	void testUnauthorizedAccept() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/accept/1"))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
	}

	@Test
	void testUnauthorizedReject() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/reject/1"))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.redirectedUrl("/"));	
	}

	@WithMockUser(username="admin", roles = {"ADMIN"})
	@Test	
	void testAuthorizedAccept() throws Exception {

		long jrC = jobRequestRepository.count();
		long uC = userRepository.count();
		long fC = fileRepository.count();

		//lets accept bonnie
		long id = 0; 
		for (JobRequest jr : jobRequestRepository.findAll()){
			if (jr.getUser().getName().equals("Bonnie")){
				id = jr.getId();
			}
		}
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/accept/" + id))
		    .andDo(MockMvcResultHandlers.print()) // Print request and response details
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(result -> {
        		assertAll(
					() -> assertEquals(jrC - 1, jobRequestRepository.count(), "Job request count should decrease by 1"),
					() -> assertEquals(uC, userRepository.count(), "User count should be equal"),
					() -> assertEquals(fC - 1, fileRepository.count(), "File count should decrease by 1")
        	);
    	});
	}

	@WithMockUser(username="admin", roles = {"ADMIN"})
	@Test	
	void testAuthorizedReject() throws Exception {

		long jrC = jobRequestRepository.count();
		long uC = userRepository.count();
		long fC = fileRepository.count();

		//lets accept bonnie
		long id = 0; 
		for (JobRequest jr : jobRequestRepository.findAll()){
			if (jr.getUser().getName().equals("Clyde")){
				id = jr.getId();
			}
		}
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/reject/" + id))
		    .andDo(MockMvcResultHandlers.print()) // Print request and response details
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(result -> {
        		assertAll(
					() -> assertEquals(jrC - 1, jobRequestRepository.count(), "Job request count should decrease by 1"),
					() -> assertEquals(uC -1 , userRepository.count(), "User count should decrease by 1"),
					() -> assertEquals(fC - 1, fileRepository.count(), "File count should decrease by 1")
        	);
    	});
	}

}
