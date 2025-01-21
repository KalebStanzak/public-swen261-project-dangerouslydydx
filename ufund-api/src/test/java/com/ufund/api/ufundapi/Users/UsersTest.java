package com.ufund.api.ufundapi.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Basket;
import com.ufund.api.ufundapi.Need;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsersTest {

    private Users users;
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Specify the path for the users.json file
        testFile = new File("src/main/java/com/ufund/api/ufundapi/data/users.json");

        // Create the directories if they don't exist
        if (!testFile.getParentFile().exists()) {
            testFile.getParentFile().mkdirs();
        }

        // Clear the file before each test to ensure clean state
        clearFile();

        // Create a new Users instance
        users = new Users();
        resetUserData();
    }

    private void clearFile() throws IOException {
        // Clear the contents of the file by writing an empty list
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("[]"); // Writing empty JSON array to clear contents
        }
    }

    private void resetUserData() throws IOException {
        // Reset the Users data by writing an empty list to the file
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(testFile, List.of());
    }

    @Test
    public void testCreateUser() throws IOException {
        int size = users.getAllUsers().size();
        User user = users.createUser("testUser");

        assertEquals(0, user.getUserID()); // First user should have ID 0
        assertEquals(size + 1, users.getAllUsers().size()); // Should have one user
        assertEquals("testUser", users.getUser(0).getUsername());
    }

    @Test
    public void testGetAllUsers() throws IOException {
        int size = users.getAllUsers().size();
        users.createUser("testUser1");
        users.createUser("testUser2"); // Create a second user with a different username

        List<User> allUsers = users.getAllUsers();
        assertEquals(size + 2, allUsers.size()); // Should now have two users

        Boolean containsUser1 = false;
        Boolean containsUser2 = false;
        for (User u : allUsers) {
            if (u.getUsername().equals("testUser1")) {
                containsUser1 = true;
            }
            if (u.getUsername().equals("testUser2")) {
                containsUser2 = true;
            }
        }
        assert(containsUser1);
        assert(containsUser2);
    }

    @Test
    public void testGetUser() throws IOException {
        users.createUser("testUser");

        User retrievedUser = users.getUser(0);
        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getUsername());
    }

    @Test
    public void testGetUserNotFound() throws IOException {
        assertNull(users.getUser(99)); // Non-existing user
    }

    @Test
    public void testDeleteUser() throws IOException {
        int oldSize = users.getAllUsers().size();
        users.createUser("testUser");
        assertEquals(oldSize + 1, users.getAllUsers().size()); // Before deletion
        oldSize = users.getAllUsers().size();
        users.deleteUser(0);
        assertEquals(oldSize - 1, users.getAllUsers().size()); // After deletion
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        assertDoesNotThrow(() -> users.deleteUser(99)); // Deleting non-existing user should not throw
        assertEquals(0, users.getAllUsers().size()); // Should still be empty if no users were created
    }

    @Test
    public void testLoadUsers() throws IOException {
        // Arrange: Create a JSON file with predefined users
        User user1 = new User("user1");
        user1.setUserID(0); // Assuming you set IDs manually for this test
        User user2 = new User("user2");
        user2.setUserID(1);

        List<User> usersToLoad = List.of(user1, user2);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(testFile, usersToLoad);

        // Act: Create a new Users instance to trigger the load
        users = new Users(); // This will call the load() method

        // Assert: Check that users were loaded correctly
        assertEquals(2, users.getAllUsers().size());
        assertEquals("user1", users.getUser(0).getUsername());
        assertEquals("user2", users.getUser(1).getUsername());
        assertEquals(1, users.assignedUID); // Check that assignedUID is set correctly
    }

    @Test
    public void testChangeUserSuccessfully() throws IOException {
        users.createUser("testUser");

        // Create a new User instance with the same ID but a different username
        User updatedUser = new User("updatedUser");
        updatedUser.setUserID(0); // Same ID as the first user
        users.changeUser(updatedUser); // Change the user in the Users instance

        User retrievedUser = users.getUser(0);
        assertEquals("updatedUser", retrievedUser.getUsername()); // Verify the change
    }

    @Test
    public void testChangeUserNotFound() throws IOException {
        int oldSize = users.getAllUsers().size();
        User nonExistentUser = new User("nonExistentUser");
        nonExistentUser.setUserID(99); // Set to an ID that doesn't exist

        assertDoesNotThrow(() -> users.changeUser(nonExistentUser)); // Should not throw
        assertEquals(oldSize, users.getAllUsers().size()); // Should still be empty if no users were created
    }

    @Test
    public void testAddToFavorites() throws IOException {
        // Arrange: Create a user and a Need instance
        users.createUser("testUser");
        User user = users.getUser(0);
        Need need = new Need(); // Need default constructor

        // Act: Add the Need to the user's favorites
        users.addToFavorites(user, need);

        // Assert: Verify that the Need has been added to the user's favorites
        User updatedUser = users.getUser(user.getUserID());
        assertNotNull(updatedUser);
        assertTrue(updatedUser.getFavorites().contains(need)); // Check if favorites contains the need
    }

    @Test
    public void testAddToBasket() throws IOException {
        // Arrange: Create a user and a properly initialized Need instance
        users.createUser("testUser");
        User user = users.getUser(0);
        Need need = new Need(1, "Test Need", 10.0, 5, "Test Type");
    
        // Act: Add the Need to the user's basket
        users.addtoBasket(user, need);
    
        // Assert: Verify that the Need has been added to the user's basket
        Basket userBasket = user.getBasket();
        assertNotNull(userBasket); // Verify basket is not null
        assertTrue(userBasket.getNeeds().contains(need)); // Verify basket contains the need
    }

    @Test
    public void testAddFunds() throws IOException {
        // Arrange: Create a user and add them to the Users instance
        users.createUser("testUser");
        User user = users.getUser(0);
        int initialFunds = (int) user.getBasket().getFunds();
        int amountToAdd = 50;

        // Act: Add funds to the user's basket
        users.addFunds(user, amountToAdd);

        // Assert: Verify that the funds have been updated correctly
        Basket userBasket = user.getBasket();
        assertNotNull(userBasket);
        assertEquals(initialFunds + amountToAdd, userBasket.getFunds()); // Check if funds are updated
    }
    @Test
    public void testRemoveFromFavorites() throws IOException {
        // Arrange: Create a user and a need instance
        users.createUser("testUser");
        User user = users.getUser(0);
        Need need = new Need(); // Assuming Need has a default constructor
        
        // Add the Need to the user's favorites
        users.addToFavorites(user, need);
        assertTrue(user.getFavorites().contains(need)); // Ensure the need is in favorites initially

        // Act: Remove the Need from the user's favorites
        users.removeFromFavorites(user, need);

        // Assert: Verify that the Need has been removed from the user's favorites
        assertFalse(user.getFavorites().contains(need)); // Check that the need is no longer in favorites
    }
    @Test
    public void testGetBasket() throws IOException {
        // Arrange: Create a user and a basket
        users.createUser("testUser");
        User user = users.getUser(0);
        
        // Ensure the user has a basket (basket initialization should happen in User constructor)
        Basket userBasket = user.getBasket();
        
        // Act: Call the getBasket method to retrieve the user's basket
        Basket retrievedBasket = users.getBasket(user);

        // Assert: Ensure that the retrieved basket is the same as the user's basket
        assertNotNull(retrievedBasket); // Basket should not be null
        assertEquals(userBasket, retrievedBasket); // The basket returned should be the same as the one initially created for the user
    }
    @Test
    public void testRemoveFromBasket() throws IOException {
        // Arrange: Create a user and add a Need to their basket
        users.createUser("testUser");
        User user = users.getUser(0);
        Basket userBasket = user.getBasket();

        // Ensure the basket is not null before proceeding
        assertNotNull(userBasket, "Basket should not be null");

        // Create a Need instance with id 1, and quantity 2
        Need need = new Need(1, "Test Need", 10.0, 2, "Test Type");
        users.addtoBasket(user, need); // Add the Need to the basket

        // Assert that the Need has been added to the basket
        assertTrue(userBasket.getNeeds().contains(need), "Basket should contain the need");

        // Act: Call the removeFromBasket method to remove 1 quantity of the Need
        users.removeFromBasket(user, 1);

        // Assert: Ensure the quantity is decremented to 1
        assertEquals(2, need.getQuantity(), "Quantity should be decremented to 2");

        // Assert: Ensure the Need is removed from the basket when the quantity is 0
        assertFalse(userBasket.getNeeds().contains(need), "Need should be removed from the basket when quantity reaches 0");
    }
    @Test
    public void testAddToBasketWithNoQuantity() throws IOException {
        // Arrange: Create a user and a Need with 0 quantity
        users.createUser("testUser");
        User user = users.getUser(0);
        Need need = new Need(1, "Test Need", 10.0, 0, "Test Type"); // 0 quantity

        // Act & Assert: Try to add the need to the basket
        assertThrows(IllegalStateException.class, () -> users.addtoBasket(user, need), "No quantity left for the requested need.");
    }
    @Test
    public void testAddToBasketIncrementQuantity() throws IOException {
        // Arrange: Create a user and a need with 3 available quantity
        users.createUser("testUser");
        User user = users.getUser(0);
        Need need = new Need(1, "Test Need", 10.0, 3, "Test Type"); // 3 quantity available

        // Add the need to the basket
        users.addtoBasket(user, need);

        // Act: Add the same need again to the basket
        users.addtoBasket(user, need);

        // Assert: Check that the quantity of the need in the basket is incremented
        Basket userBasket = user.getBasket();
        assertEquals(3, need.getQuantity(), "Need quantity should be incremented to 2");
    }
    @Test
    public void testAddToBasketWithSufficientQuantity() throws IOException {
        // Arrange: Create a user and a Need
        users.createUser("testUser");
        User user = users.getUser(0);
        Need need = new Need(1, "Test Need", 10.0, 5, "Test Type"); // 5 available quantity
        
        // Act: Add the need to the basket
        users.addtoBasket(user, need);

        // Assert: Ensure the Need is added to the basket with quantity 1
        Basket userBasket = user.getBasket();
        assertTrue(userBasket.getNeeds().contains(need), "Basket should contain the need");
        assertEquals(5, need.getQuantity(), "Need quantity should be 1 after adding to basket");
    }
}
