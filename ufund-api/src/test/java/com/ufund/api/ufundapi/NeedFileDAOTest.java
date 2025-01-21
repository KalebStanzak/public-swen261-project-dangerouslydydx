package com.ufund.api.ufundapi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class NeedFileDAOTest {
    private NeedFileDAO needFileDAO;
    private File testFile;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        testFile = File.createTempFile("needs", ".json");
        testFile.deleteOnExit(); // Ensure the temp file is deleted after tests

        // Set up test data in JSON format
        Need[] needs = {
            new Need(1, "Toys", 10.0, 4, "Tennis Ball"),
            new Need(2, "Food", 5.0, 2, "Dog")
        };
        objectMapper.writeValue(testFile, needs);

        needFileDAO = new NeedFileDAO(testFile.getAbsolutePath(), objectMapper);
    }

    @AfterEach
    public void tearDown() {
        testFile.delete(); // Clean up test file if not deleted automatically
    }

    @Test
    public void testGetNeeds() {
        Need[] result = needFileDAO.getNeeds();
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    public void testFindNeeds() {
        Need[] result = needFileDAO.findNeeds("Toys");
        assertEquals(1, result.length);
        assertEquals("Toys", result[0].getName());
    }

    @Test
    public void testCreateNeed() throws IOException {
        Need newNeed = new Need(0, "Books", 15.0, 3, "Education");
        Need createdNeed = needFileDAO.createNeed(newNeed);
        assertNotNull(createdNeed);
        assertEquals("Books", createdNeed.getName());
    }
    @Test
    public void testUpdateNeed() throws IOException {
        Need updatedNeed = new Need(1, "Toys", 12.0, 4, "Tennis Ball");
        Need result = needFileDAO.updateNeed(updatedNeed);
    
        assertNotNull(result);
        assertEquals(12.0, result.getCost());
        assertEquals("Toys", result.getName());
    }
    @Test
    public void testUpdateNeed_NonExistent() throws IOException {
        Need nonExistentNeed = new Need(99, "Nonexistent", 10.0, 1, "TypeX");
        Need result = needFileDAO.updateNeed(nonExistentNeed);
    
        assertNull(result);
    }
    @Test
    public void testIsNameUnique_True() {
        assertTrue(needFileDAO.isNameUnique("Unique Name"));
    }
    @Test
    public void testIsNameUnique_False() throws IOException {
        // Create a need with the name "Toys"
        Need existingNeed = new Need(1, "Toys", 10.0, 4, "Tennis Ball");
        needFileDAO.createNeed(existingNeed);
        
        // Now check if "Toys" is unique
        assertFalse(needFileDAO.isNameUnique("Toys"));
    }
    @Test
    public void testGetNeed_ExistingID() {
        Need need = needFileDAO.getNeed(1);
        assertNotNull(need);
        assertEquals("Toys", need.getName());
    }

    @Test
    public void testGetNeed_NonExistentID() {
        Need need = needFileDAO.getNeed(99);
        assertNull(need);
    }
    @Test
    public void testDeleteNeed_Success() throws IOException {
        // Create a need and ensure it exists
        Need existingNeed = new Need(1, "Toys", 10.0, 4, "Tennis Ball");
        needFileDAO.createNeed(existingNeed);
        
        // Delete the need
        boolean deleted = needFileDAO.deleteNeed(existingNeed.getId());
        assertTrue(deleted);
    
        // Verify that the need no longer exists
        Need retrievedNeed = needFileDAO.getNeed(existingNeed.getId());
        assertNull(retrievedNeed);
    }
    
    @Test
    public void testDeleteNeed_NonExistent() throws IOException {
        // Attempt to delete a need with an ID that doesn't exist
        boolean deleted = needFileDAO.deleteNeed(99);
        assertFalse(deleted);
    }
    @Test
    public void testLoad_SetsNextIdCorrectly() throws IOException {
        // Create a JSON file with Needs having specific IDs
        Need[] needs = {
            new Need(1, "Toys", 10.0, 4, "Tennis Ball"),
            new Need(2, "Food", 5.0, 2, "Dog"),
            new Need(3, "Books", 15.0, 3, "Education")
        };
        objectMapper.writeValue(testFile, needs); // Write these needs to the temp file

        // Create NeedFileDAO, which should load the needs
        needFileDAO = new NeedFileDAO(testFile.getAbsolutePath(), objectMapper);

        // Now create a new Need and verify the ID
        Need newNeed = new Need(0, "New Item", 20.0, 1, "Miscellaneous");
        Need createdNeed = needFileDAO.createNeed(newNeed);

        // Since the highest ID was 3, the new need should have ID 4
        assertNotNull(createdNeed);
        assertEquals(4, createdNeed.getId());
    }
    
}
