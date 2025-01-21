package com.ufund.api.ufundapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class BasketPersistence {

    // Define the file path for the basket.json file (use resources directory)
    private static final String BASKET_FILE_PATH = "src/main/resources/data/basket.json";

    // Save a basket object to the file
    public static void saveBasket(Basket basket) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create the file and directories if they don't exist
        File basketFile = new File(BASKET_FILE_PATH);
        if (!basketFile.exists()) {
            basketFile.getParentFile().mkdirs();  // Create parent directories if needed
            basketFile.createNewFile();           // Create the basket.json file
        }

        // Write the basket object to the file
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(basketFile, basket);
    }

    // Load a basket object from the file
    public static Basket loadBasket() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File basketFile = new File(BASKET_FILE_PATH);

        if (!basketFile.exists()) {
            // If the basket file doesn't exist, create a default basket
            Basket defaultBasket = new Basket(1, "Default Basket", 1000.0); // Default basket values
            saveBasket(defaultBasket); // Save the default basket to the file
            return defaultBasket;      // Return the default basket
        }

        // If the file exists, read and return the Basket object
        return objectMapper.readValue(basketFile, Basket.class);
    }
}
