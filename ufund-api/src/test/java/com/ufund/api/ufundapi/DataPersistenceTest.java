package com.ufund.api.ufundapi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DataPersistenceTest {
    private BasketPersistence dataPersistence;
    private File basketFile;

    @BeforeEach
    public void setUp() {
        dataPersistence = new BasketPersistence();
        basketFile = new File("src/main/resources/data/basket.json");
        basketFile.delete(); // Ensure the file does not exist before the test
    }

    @AfterEach
    public void tearDown() {
        basketFile.delete(); // Clean up after tests
    }

    @Test
    public void testSaveBasket() throws IOException {
        // Create a sample Basket object
        Basket basket = new Basket(); // Populate your Basket object as needed
        // Example: basket.addItem(new Item(...));

        // Save the basket
        BasketPersistence.saveBasket(basket);

        // Verify that the file was created and has content
        assertNotNull(basketFile);
        assertEquals(true, basketFile.exists(), "Basket file should exist after saving");
    }

    @Test
    public void testLoadBasket() throws IOException {
        // Create and save a sample Basket
        Basket basket = new Basket(); // Populate your Basket object as needed
        // Example: basket.addItem(new Item(...));
        BasketPersistence.saveBasket(basket);

        // Now load the basket
        Basket loadedBasket = BasketPersistence.loadBasket();
        assertNotNull(loadedBasket, "Loaded basket should not be null");

        // Add assertions to check properties of loadedBasket
        // For example: assertEquals(expectedValue, loadedBasket.getSomeProperty());
    }
}
