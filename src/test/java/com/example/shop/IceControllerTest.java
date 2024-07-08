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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.mock.web.MockMultipartFile;

import com.example.shop.model.File;
import com.example.shop.model.Flavour;
import com.example.shop.model.JobRequest;
import com.example.shop.model.User;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.CupRepository;
import com.example.shop.repository.FlavourRepository;
import com.example.shop.repository.PricingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IceControllerTest {
        
    @Autowired
    private CupRepository cupRepository;

    @Autowired
    private FlavourRepository flavourRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PricingRepository pricingRepository;

    @Autowired
    private MockMvc mockMvc;

    static final Path fdir = Path.of("src", "main", "resources", "static", "images", "flavours");

    @BeforeEach
	@WithMockUser(username="admin")
    public void setUp() throws Exception{

		if (!Files.exists(fdir)) {
			Files.createDirectories(fdir);
		}

        Flavour flavour = new Flavour("IchigoSando", false, "Delicious flavor");
        flavour.setPricing(pricingRepository.findByDescripton("Spoon").get(0));
        flavour = flavourRepository.save(flavour);

        String fileName = flavour.getId() + ".png" ;
        Path filePath = fdir.resolve(fileName);
        Files.write(filePath, "IchigoSando".getBytes());

        com.example.shop.model.File file = new com.example.shop.model.File(fileName, filePath.toString(), "png");
        fileRepository.save(file);

        flavour.setFile(file);
        flavourRepository.save(flavour);
    }

	@AfterEach
	public void tearDown() throws Exception{
        
        try{
        Flavour flav1 = flavourRepository.findByName("IchigoSando").get(0);
            if (Files.exists(Paths.get(flav1.getFile().getUrl()).normalize())) {
                Paths.get(flav1.getFile().getUrl()).normalize().toFile().delete();
            }
            flavourRepository.delete(flav1);
        } catch (Exception e) { 
        }
        try{
            Flavour flav2 = flavourRepository.findByName("Vanilla").get(0);
            if (Files.exists(Paths.get(flav2.getFile().getUrl()).normalize())) {
                Paths.get(flav2.getFile().getUrl()).normalize().toFile().delete();
            }
            flavourRepository.delete(flav2);
        } catch (Exception e) {
        }
	}

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testAddFlavourNoImage() throws Exception {
        // Need a File object to be created first
        MockMultipartFile file = new MockMultipartFile("image", "something.pdf",  "application/pdf", "I'm a pdf".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ice/addFlavour")
            .file(file)
            .param("name", "Vanilla")  
            .param("isVegan", "true")  
            .param("description", "Delicious vanilla flavor") 
            .param("pricing.id", "1") 
            .param("file.id", "1")
            .param("description", "Vanilla flavour"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testAddFlavour() throws Exception {
        // Need a File object to be created first
        MockMultipartFile file = new MockMultipartFile("image", "something.png", MediaType.IMAGE_PNG_VALUE, "I'm a pdf".getBytes());

        long flC = flavourRepository.count();
        long fileC = fileRepository.count();


        mockMvc.perform(MockMvcRequestBuilders.multipart("/ice/addFlavour")
            .file(file)
            .param("name", "Vanilla")  
            .param("isVegan", "true")  
            .param("description", "Delicious vanilla flavor") 
            .param("pricing.id", "1") 
            .param("file.id", "1")
            .param("description", "Vanilla flavour"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(result -> {
        		assertAll(
					() -> assertEquals(fileC + 1, fileRepository.count(), "Files count should increase by 1"),
					() -> assertEquals(flC + 1, flavourRepository.count(), "Flavours count should increase by 1")
        	    );
            });
    }

    @Test
    @Transactional
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testDeleteFlavour() throws Exception {
        Flavour flavour = flavourRepository.findByName("IchigoSando").get(0);

        long flC = flavourRepository.count();
        long fileC = fileRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.delete("/ice/deleteFlavour/" + flavour.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(result -> {
        		assertAll(
					() -> assertEquals(fileC - 1, fileRepository.count(), "Files count should decrease by 1"),
					() -> assertEquals(flC - 1, flavourRepository.count(), "Flavours count should decrease by 1")
        	    );
            });
    }


    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testDeleteFlavourWrongId() throws Exception {
        long id = 0;
        while (flavourRepository.existsById(id)) {
            id++;
        }

        mockMvc.perform(MockMvcRequestBuilders.delete("/ice/deleteFlavour/" + id))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
