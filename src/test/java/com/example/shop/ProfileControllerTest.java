package com.example.shop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
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

	static final Path cvdir = Path.of("src", "test", "resources");


	@BeforeEach
	@WithMockUser(username="admin")
    public void setUp() throws Exception{

		if (!Files.exists(cvdir)) {
			Files.createDirectories(cvdir);
		}

		// Jesus lord, I need factories for that... 
		User bonnie = new User("Bonnie", "Elizabeth Parker", "Bonnie@example.com", "pending");
		bonnie.setPassword("admin");
		bonnie = userRepository.save(bonnie);

		Path filePath1 = cvdir.resolve(bonnie.getId() + ".pdf");
		Files.write(filePath1, "test".getBytes());
	
		User clyde = new User("Clyde", "Chestnut Barrow", "Clyde@example.com", "pending");
		clyde.setPassword("admin");
		clyde = userRepository.save(clyde);

		Path filePath2 = cvdir.resolve(clyde.getId() + ".pdf");
		Files.write(filePath2, "test".getBytes());
	
		File bonnieCV = new File(bonnie.getId() + ".pdf", filePath1.toString(), "pdf");	
		File clydeCV = new File(clyde.getId() + ".pdf", filePath2.toString(), "pdf");
	
		JobRequest jobRequestBonnie = new JobRequest(bonnie, bonnieCV, "pending", "miniJob");
		jobRequestBonnie = jobRequestRepository.save(jobRequestBonnie);
	
		JobRequest jobRequestClyde = new JobRequest(clyde, clydeCV, "pending", "miniJob");
		jobRequestClyde = jobRequestRepository.save(jobRequestClyde);
    }

	@AfterEach
	public void tearDown() throws Exception{

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

		if (Files.exists(cvdir)) {
			Files.delete(cvdir);
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
	void testAuthorizedDownload() throws Exception {
		long id = jobRequestRepository.findByEmail("Bonnie@example.com").get(0).getId(); 
		
		mockMvc.perform(MockMvcRequestBuilders.get("/download/cv/" + id))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(username="admin", roles = {"ADMIN"})
	@Test	
	void testAuthorizedDownloadWrongId() throws Exception {
		long id = 0;
		while (jobRequestRepository.existsById(id)){
			id++;
		}
		
		mockMvc.perform(MockMvcRequestBuilders.get("/download/cv/" + id))
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@WithMockUser(username="admin", roles = {"ADMIN"})
	@Test	
	void testAuthorizedAccept() throws Exception {

		long jrC = jobRequestRepository.count();
		long uC = userRepository.count();
		long fC = fileRepository.count();

		long id = jobRequestRepository.findByEmail("Bonnie@example.com").get(0).getId(); 
		
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
	void testAuthorizedAcceptWrongId() throws Exception {

		long jrC = jobRequestRepository.count();
		long uC = userRepository.count();
		long fC = fileRepository.count();

		long id = 0;
		while (jobRequestRepository.existsById(id)){
			id++;
		}

		mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/accept/" + id))
		    .andDo(MockMvcResultHandlers.print()) // Print request and response details
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(result -> {
        		assertAll(
					() -> assertEquals(jrC, jobRequestRepository.count(), "Job request count should be equal"),
					() -> assertEquals(uC, userRepository.count(), "User count should be equal"),
					() -> assertEquals(fC, fileRepository.count(), "File count should be equal")
        	);
    	});
	}

	@WithMockUser(username="admin", roles = {"ADMIN"})
	@Test	
	void testAuthorizedReject() throws Exception {

		long jrC = jobRequestRepository.count();
		long uC = userRepository.count();
		long fC = fileRepository.count();

		long id = jobRequestRepository.findByEmail("Clyde@example.com").get(0).getId(); 

		mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/reject/" + id))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(result -> {
        		assertAll(
					() -> assertEquals(jrC - 1, jobRequestRepository.count(), "Job request count should decrease by 1"),
					() -> assertEquals(uC -1 , userRepository.count(), "User count should decrease by 1"),
					() -> assertEquals(fC - 1, fileRepository.count(), "File count should decrease by 1")
        	);
    	});
	}

	@WithMockUser(username="admin", roles = {"ADMIN"})
	@Test	
	void testAuthorizedRejectWrongId() throws Exception {

		long jrC = jobRequestRepository.count();
		long uC = userRepository.count();
		long fC = fileRepository.count();

		//lets accept bonnie
		long id = 0; 
		while (jobRequestRepository.existsById(id)) {
			id++;
		}

		mockMvc.perform(MockMvcRequestBuilders.delete("/jobs/reject/" + id))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(result -> {
        		assertAll(
					() -> assertEquals(jrC, jobRequestRepository.count(), "Job request count should be equal"),
					() -> assertEquals(uC, userRepository.count(), "User count should be equal"),
					() -> assertEquals(fC, fileRepository.count(), "File count should be equal")
        	);
    	});
	}

}
