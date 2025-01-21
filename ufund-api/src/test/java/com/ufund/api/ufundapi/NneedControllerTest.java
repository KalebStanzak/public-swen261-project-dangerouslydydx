package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class NneedControllerTest {

    @Mock
    private NeedDAO needDao;

    @InjectMocks
    private NeedController needController;
    private Need need1;
    private Need need2;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    
        // Create your need objects for use in the tests
        need1 = new Need(1, "dog treats", 15.0, 10, "treats");
        need2 = new Need(2, "cat litter", 20.0, 10, "cleaning");
    
        // Example of proper mock setup without using argument matchers before method invocations
        when(needDao.getNeed(1)).thenReturn(need1);  // Mock getNeed to return need1
        when(needDao.getNeeds()).thenReturn(new Need[] { need1, need2 }); // Mock getNeeds to return a list
        when(needDao.createNeed(any(Need.class))).thenReturn(need1); // Use any() for the argument
        when(needDao.updateNeed(any(Need.class))).thenReturn(need1); // Use any() for the argument
        when(needDao.deleteNeed(1)).thenReturn(true);  // Correctly setup deleteNeed
    
        // No verification here, just setup
    }

    @Test
    public void testGetNeed() throws IOException {
        when(needDao.getNeed(1)).thenReturn(need1);

        ResponseEntity<Need> response = needController.getNeed(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need1, response.getBody());
    }

    @Test
    public void testGetNeeds() throws IOException {
        Need[] needs = {need1, need2};
        when(needDao.getNeeds()).thenReturn(needs);

        ResponseEntity<Need[]> response = needController.getNeeds();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(needs, response.getBody());
    }

    @Test
    public void testCreateNeed() throws IOException {
        // Correct usage of matchers for the argument
        when(needDao.createNeed(any(Need.class))).thenReturn(need1);

        ResponseEntity<Need> response = needController.createNeed(need1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(need1, response.getBody());
    }

    @Test
    public void testUpdateNeed() throws IOException {
        when(needDao.getNeed(1)).thenReturn(need1);
        when(needDao.updateNeed(any(Need.class))).thenReturn(need1);

        ResponseEntity<Need> response = needController.updateNeed(need1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need1, response.getBody());
    }

    @Test
    public void testDeleteNeed() throws IOException {
        when(needDao.getNeed(1)).thenReturn(need1);
        when(needDao.deleteNeed(1)).thenReturn(true);

        ResponseEntity<Need> response = needController.deleteNeed(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetNeedNotFound() throws IOException {
        when(needDao.getNeed(1)).thenReturn(null);

        ResponseEntity<Need> response = needController.getNeed(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetNeedsEmpty() throws IOException {
        when(needDao.getNeeds()).thenReturn(new Need[0]);

        ResponseEntity<Need[]> response = needController.getNeeds();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(new Need[0], response.getBody());
    }

    @Test
    public void testUpdateNeedNotFound() throws IOException {
        when(needDao.getNeed(1)).thenReturn(null);

        ResponseEntity<Need> response = needController.updateNeed(need1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException {
        when(needDao.getNeed(1)).thenReturn(null);

        ResponseEntity<Need> response = needController.deleteNeed(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testIOExceptionOnGetNeed() throws IOException {
        when(needDao.getNeed(1)).thenThrow(new IOException("Database error"));

        ResponseEntity<Need> response = needController.getNeed(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchNeedsFound() throws IOException {
        // Simulate that getNeeds is called and returns a non-null value
        when(needDao.getNeeds()).thenReturn(new Need[] { need1, need2 });
        // Simulate findNeeds to return the expected needs for the search term
        when(needDao.findNeeds("dog")).thenReturn(new Need[] { need1 });

        ResponseEntity<Need[]> response = needController.searchNeeds("dog");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(new Need[] { need1 }, response.getBody());
    }

    @Test
    public void testSearchNeedsNotFound() throws IOException {
        // Simulate that getNeeds is called and returns a non-null value
        when(needDao.getNeeds()).thenReturn(new Need[] { need1, need2 });
        // Simulate findNeeds to return an empty array for the search term
        when(needDao.findNeeds("nonexistent")).thenReturn(new Need[0]);

        ResponseEntity<Need[]> response = needController.searchNeeds("nonexistent");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(new Need[0], response.getBody()); // Expect an empty array
    }

    @Test
    public void testGetNeedsNotFound() throws IOException {
        // Mock the DAO to return null for getNeeds
        when(needDao.getNeeds()).thenReturn(null);

        ResponseEntity<Need[]> response = needController.getNeeds();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetNeedsInternalServerError() throws IOException {
        // Mock the DAO to throw an IOException
        when(needDao.getNeeds()).thenThrow(new IOException("Database error"));

        ResponseEntity<Need[]> response = needController.getNeeds();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchNeedsInternalServerError() throws IOException {
        // Mock getNeeds to throw an IOException
        when(needDao.getNeeds()).thenThrow(new IOException("Database error"));

        ResponseEntity<Need[]> response = needController.searchNeeds("someName");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateNeedNotFound() throws IOException {
        // Mock the DAO to return null when creating the need
        when(needDao.createNeed(any(Need.class))).thenReturn(null);

        ResponseEntity<Need> response = needController.createNeed(need1);

        // Update the expected status
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    public void testCreateNeedInternalServerError() throws IOException {
        // Mock the DAO to throw an IOException
        when(needDao.createNeed(any(Need.class))).thenThrow(new IOException("Database error"));

        ResponseEntity<Need> response = needController.createNeed(need1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateNeedInternalServerError() throws IOException {
        // Set up a need object to update
        Need needToUpdate = new Need(1, "dog treats", 15.0, 10, "treats");

        // Mock getNeed to return the need object
        when(needDao.getNeed(needToUpdate.getId())).thenReturn(needToUpdate);

        // Mock updateNeed to throw an IOException
        when(needDao.updateNeed(any(Need.class))).thenThrow(new IOException("Database error"));

        ResponseEntity<Need> response = needController.updateNeed(needToUpdate);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedInternalServerError() throws IOException {
        // Mock the id of the need to delete
        int needId = 1;

        // Mock getNeed to return a valid need object
        Need needToDelete = new Need(needId, "dog treats", 15.0, 10, "treats");
        when(needDao.getNeed(needId)).thenReturn(needToDelete);

        // Mock deleteNeed to throw an IOException
        when(needDao.deleteNeed(needId)).thenThrow(new IOException("Database error"));

        ResponseEntity<Need> response = needController.deleteNeed(needId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchNeedsNotFoundWithException() throws IOException {
        String searchName = "dog";

        // Mock getNeeds to return null (no needs found)
        when(needDao.getNeeds()).thenReturn(null);

        ResponseEntity<Need[]> response = needController.searchNeeds(searchName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
