package com.example.shop;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

	// For this layer, a user should at least be authenticated to access the resources, so the tests are doubled

	@Autowired
    private WebApplicationContext context;

	@Autowired
	private MockMvc mockMvc;

	// @Before
    // public void setup() {
	// 	mockMvc = MockMvcBuilders
    //       .webAppContextSetup(context)
    //       .apply(springSecurity())
    //       .build();
    // }

	
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

}
