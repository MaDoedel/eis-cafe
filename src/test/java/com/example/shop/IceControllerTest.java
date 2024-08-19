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
import com.example.shop.model.Sauce;
import com.example.shop.model.User;
import com.example.shop.model.Topping;
import com.example.shop.model.Fruit;
import com.example.shop.repository.FileRepository;
import com.example.shop.repository.CupRepository;
import com.example.shop.repository.FlavourRepository;
import com.example.shop.repository.PricingRepository;
import com.example.shop.repository.ToppingRepository;
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
    private ToppingRepository toppingRepository;

    @Autowired
    private MockMvc mockMvc;

    static final Path fdir = Path.of("src", "main", "resources", "static", "images", "flavours");
    static final Path cdir = Path.of("src", "main", "resources", "static", "images", "cups");
    static final Path tfdir = Path.of("src", "main", "resources", "static", "images", "toppings", "fruits");
    static final Path tsdir = Path.of("src", "main", "resources", "static", "images", "toppings", "sauce");
    static final Path tcdir = Path.of("src", "main", "resources", "static", "images", "toppings", "candies");



    @BeforeEach
	@WithMockUser(username="admin")
    public void setUp() throws Exception{

        fileRepository.deleteAll();

		if (!Files.exists(fdir)) {
			Files.createDirectories(fdir);
		}

        if (!Files.exists(tfdir)) {
			Files.createDirectories(tfdir);
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

        Fruit fruit = new Fruit("Banana", "Delicious fruit");
        fruit.setPricing(pricingRepository.findByDescripton("Fruit").get(0));
        fruit = toppingRepository.save(fruit);

        String fileName2 = fruit.getId() + ".png" ;
        Path filePath2 = tfdir.resolve(fileName2);
        Files.write(filePath2, "Banana".getBytes());

        com.example.shop.model.File file2 = new com.example.shop.model.File(fileName2, filePath2.toString(), "png");
        fileRepository.save(file2);

        fruit.setFile(file2);
        toppingRepository.save(fruit);

        Sauce sauce = new Sauce("Some Sauce", "Delicious sauce", false);
        sauce.setPricing(pricingRepository.findByDescripton("Sauce").get(0));
        sauce = toppingRepository.save(sauce);

        String fileName3 = sauce.getId() + ".png" ;
        Path filePath3 = tsdir.resolve(fileName3);
        Files.write(filePath3, "Some Sauce".getBytes());

        com.example.shop.model.File file3 = new com.example.shop.model.File(fileName3, filePath3.toString(), "png");
        fileRepository.save(file3);
        
        sauce.setFile(file3);
        toppingRepository.save(sauce);
        
        Candy candy = new Candy("Chips", "Delicious candy", false);
        candy.setPricing(pricingRepository.findByDescripton("Candy").get(0));
        candy = toppingRepository.save(candy);

        String fileName4 = candy.getId() + ".png" ;
        Path filePath4 = tcdir.resolve(fileName4);
        Files.write(filePath4, "Chips".getBytes());

        com.example.shop.model.File file4 = new com.example.shop.model.File(fileName4, filePath4.toString(), "png");
        fileRepository.save(file4);

        candy.setFile(file4);
        toppingRepository.save(candy);

        Cup cup = new Cup("Some cup", new BigDecimal(5.00), "Delicious cup");
        cup.setFlavours(new ArrayList<Flavour>());
        cup.setToppings(new ArrayList<Topping>());
        cup = cupRepository.save(cup);

        String fileName5 = cup.getId() + ".png" ;
        Path filePath5 = cdir.resolve(fileName5);
        Files.write(filePath5, "Cup".getBytes());

        com.example.shop.model.File file5 = new com.example.shop.model.File(fileName5, filePath5.toString(), "png");
        fileRepository.save(file5);

        cup.setFile(file5);
        cupRepository.save(cup);
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

        try{
            Topping fr1 = toppingRepository.findByName("Apple").get(0);
            if (Files.exists(Paths.get(fr1.getFile().getUrl()).normalize())) {
                Paths.get(fr1.getFile().getUrl()).normalize().toFile().delete();
            }
            toppingRepository.delete(fr1);
        } catch (Exception e) {
        }
        try{
            Topping fr2 = toppingRepository.findByName("Banana").get(0);
            if (Files.exists(Paths.get(fr2.getFile().getUrl()).normalize())) {
                Paths.get(fr2.getFile().getUrl()).normalize().toFile().delete();
            }
            toppingRepository.delete(fr2);
        } catch (Exception e) {
        }
        try{
            Topping fr3 = toppingRepository.findByName("Kinder Bueno").get(0);
            if (Files.exists(Paths.get(fr3.getFile().getUrl()).normalize())) {
                Paths.get(fr3.getFile().getUrl()).normalize().toFile().delete();
            }
            toppingRepository.delete(fr3);
        } catch (Exception e) {
        }
        try{
            Topping fr5 = toppingRepository.findByName("Chips").get(0);
            if (Files.exists(Paths.get(fr5.getFile().getUrl()).normalize())) {
                Paths.get(fr5.getFile().getUrl()).normalize().toFile().delete();
            }
            toppingRepository.delete(fr5);
        } catch (Exception e) {
        }

        try{
            Topping fr6 = toppingRepository.findByName("Some Sauce").get(0);
            if (Files.exists(Paths.get(fr6.getFile().getUrl()).normalize())) {
                Paths.get(fr6.getFile().getUrl()).normalize().toFile().delete();
            }
            toppingRepository.delete(fr6);
        } catch (Exception e) {
        }
        try{
            Topping fr7 = toppingRepository.findByName("Caramel Sauce").get(0);
            if (Files.exists(Paths.get(fr7.getFile().getUrl()).normalize())) {
                Paths.get(fr7.getFile().getUrl()).normalize().toFile().delete();
            }
            toppingRepository.delete(fr7);
        } catch (Exception e) {
        }

        try{
            Cup fr8 = cupRepository.findByName("Banana Caramel Delight").get(0);
            if (Files.exists(Paths.get(fr8.getFile().getUrl()).normalize())) {
                Paths.get(fr8.getFile().getUrl()).normalize().toFile().delete();
            }
            cupRepository.delete(fr8);
        } catch (Exception e) {
        }

        try{
            Cup fr9 = cupRepository.findByName("Some cup").get(0);
            if (Files.exists(Paths.get(fr9.getFile().getUrl()).normalize())) {
                Paths.get(fr9.getFile().getUrl()).normalize().toFile().delete();
            }
            cupRepository.delete(fr9);
        } catch (Exception e) {
        }
	}

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testAddFlavourNoImage() throws Exception {
        // TODO: Fix this test
        MockMultipartFile file = new MockMultipartFile("image", "something.pdf",  "application/pdf", "I'm a pdf".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ice/addFlavour")
            .file(file)
            .param("name", "Vanilla")  
            .param("isVegan", "true")  
            .param("pricing.id", "1") 
            .param("file.id", "1")
            .param("description", "Vanilla flavour"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testAddFlavour() throws Exception {
        // Need a File object to be created first
        MockMultipartFile file = new MockMultipartFile("image", "something.png", MediaType.IMAGE_PNG_VALUE, "I'm a png".getBytes());

        long flC = flavourRepository.count();
        long fileC = fileRepository.count();


        mockMvc.perform(MockMvcRequestBuilders.multipart("/ice/addFlavour")
            .file(file)
            .param("name", "Vanilla")  
            .param("isVegan", "true")  
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

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testAddToppingFruit() throws Exception {
        // Need a File object to be created first
        MockMultipartFile file = new MockMultipartFile("image", "something.png", MediaType.IMAGE_PNG_VALUE, "I'm a png".getBytes());

        long trC = toppingRepository.count();
        long fileC = fileRepository.count();


        mockMvc.perform(MockMvcRequestBuilders.multipart("/ice/addFruit")
            .file(file)
            .param("name", "Apple")  
            .param("description", "Delicious fruit")
            .param("pricing.id", "2") 
            .param("file.id", "1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(result -> {
        		assertAll(
					() -> assertEquals(fileC + 1, fileRepository.count(), "Files count should increase by 1"),
					() -> assertEquals(trC + 1, toppingRepository.count(), "Topping count should increase by 1")
        	    );
            });
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testAddToppingCandy() throws Exception {
        // Need a File object to be created first
        MockMultipartFile file = new MockMultipartFile("image", "something.png", MediaType.IMAGE_PNG_VALUE, "I'm a png".getBytes());

        long trC = toppingRepository.count();
        long fileC = fileRepository.count();


        mockMvc.perform(MockMvcRequestBuilders.multipart("/ice/addCandy")
            .file(file)
            .param("name", "Kinder Bueno")  
            .param("description", "Delicious candy")
            .param("pricing.id", "2") 
            .param("file.id", "1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(result -> {
        		assertAll(
					() -> assertEquals(fileC + 1, fileRepository.count(), "Files count should increase by 1"),
					() -> assertEquals(trC + 1, toppingRepository.count(), "Topping count should increase by 1")
        	    );
            });
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testAddToppingSauce() throws Exception {
        // Need a File object to be created first
        MockMultipartFile file = new MockMultipartFile("image", "something.png", MediaType.IMAGE_PNG_VALUE, "I'm a png".getBytes());

        long trC = toppingRepository.count();
        long fileC = fileRepository.count();


        mockMvc.perform(MockMvcRequestBuilders.multipart("/ice/addSauce")
            .file(file)
            .param("name", "Caramel Sauce")  
            .param("description", "Delicious sauce")
            .param("pricing.id", "3") 
            .param("file.id", "1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(result -> {
        		assertAll(
					() -> assertEquals(fileC + 1, fileRepository.count(), "Files count should increase by 1"),
					() -> assertEquals(trC + 1, toppingRepository.count(), "Topping count should increase by 1")
        	    );
            });
    }

    @Test
    @Transactional
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testDeleteToppingFruit() throws Exception {
        Topping banana = toppingRepository.findByName("Banana").get(0);

        long trC = toppingRepository.count();
        long fileC = fileRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.delete("/ice/deleteTopping/" + banana.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(result -> {
        		assertAll(
					() -> assertEquals(fileC - 1, fileRepository.count(), "Files count should decrease by 1"),
					() -> assertEquals(trC - 1, toppingRepository.count(), "Topping count should decrease by 1")
        	    );
            });
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testDeleteToppingWrongId() throws Exception {
        long id = 0;
        while (toppingRepository.existsById(id)) {
            id++;
        }

        mockMvc.perform(MockMvcRequestBuilders.delete("/ice/deleteTopping/" + id))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testDeleteToppingSauce() throws Exception {
        Topping sauce = toppingRepository.findByName("Some Sauce").get(0);

        long trC = toppingRepository.count();
        long fileC = fileRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.delete("/ice/deleteTopping/" + sauce.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(result -> {
        		assertAll(
					() -> assertEquals(fileC - 1, fileRepository.count(), "Files count should decrease by 1"),
					() -> assertEquals(trC - 1, toppingRepository.count(), "Topping count should decrease by 1")
        	    );
            });
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testDeleteToppingCandy() throws Exception {
        Topping chips = toppingRepository.findByName("Chips").get(0);

        long trC = toppingRepository.count();
        long fileC = fileRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.delete("/ice/deleteTopping/" + chips.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(result -> {
        		assertAll(
					() -> assertEquals(fileC - 1, fileRepository.count(), "Files count should decrease by 1"),
					() -> assertEquals(trC - 1, toppingRepository.count(), "Topping count should decrease by 1")
        	    );
            });
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testAddCup() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "something.png", MediaType.IMAGE_PNG_VALUE, "I'm a png".getBytes());

        Flavour flavour = flavourRepository.findByName("IchigoSando").get(0);
        Topping fruit = toppingRepository.findByName("Banana").get(0);

        long cC = cupRepository.count();
        //long fileC = fileRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ice/addCup")
                .file(file)
                .param("flavour[" + flavour.getId() +"]", "2") 
                .param("topping["+ fruit.getId() +"]", "1") 
                .param("CupName", "Banana Caramel Delight")
                .param("CupPrice", "5.00")
                .param("CupDescription", "Test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    assertAll(
                        //() -> assertEquals(fileC + 1, fileRepository.count(), "Files count should increase by 1"),
                        () -> assertEquals(cC + 1, cupRepository.count(), "Cup count should increase by 1")
                    );
                });
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testDeleteCup() throws Exception {
        Cup cup = cupRepository.findByName("Some cup").get(0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/ice/deleteCup/" + cup.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username ="admin", roles={"ADMIN"})
    public void testDeleteCupWrongID() throws Exception {
        long id = 0;
        while (cupRepository.existsById(id)) {
            id++;
        }

        mockMvc.perform(MockMvcRequestBuilders.delete("/ice/deleteCup/" + id))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
