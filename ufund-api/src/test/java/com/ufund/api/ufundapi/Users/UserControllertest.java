package com.ufund.api.ufundapi.Users;

import com.ufund.api.ufundapi.Basket;
import com.ufund.api.ufundapi.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private Users users;

    private User testUser;

    private Need testNeed;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("testUser");
        testUser.setUserID(1);
        testUser.setPermissions(3);
    }
    @Test
    public void testCreateUser_UserAlreadyExists() throws IOException {
        // Mock existing user in the list returned by users.getAllUsers()
        User existingUser = new User("testUser");
        when(users.getAllUsers()).thenReturn(List.of(existingUser));
    
        // Call the createUser method
        ResponseEntity<User> response = userController.createUser("testUser");
    
        // Assert that the response status is CONFLICT
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    @Test
    public void testCreateUser_Success() throws IOException {
        // Mock that no users exist yet
        when(users.getAllUsers()).thenReturn(List.of());

        // Mock the behavior of createUser to return the newly created user
        User testUser = new User("testUser");
        when(users.createUser("testUser")).thenReturn(testUser);

        // Call the createUser method
        ResponseEntity<User> response = userController.createUser("testUser");

        // Assert the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        
        // Verify that the createUser method was called
        verify(users).createUser("testUser");
    }


    @Test
    public void testCreateUser_InternalServerError() throws IOException {
        // Mock that no users exist yet
        when(users.getAllUsers()).thenReturn(List.of());
        
        // Mock the createUser method to throw an IOException
        doThrow(new IOException()).when(users).createUser("testUser");
    
        // Call the createUser method
        ResponseEntity<User> response = userController.createUser("testUser");
    
        // Assert the response status is INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    

    @Test
    public void testGetUser_UserExists() {
        // Create a mock user with the username "testUser"
        User testUser = new User("testUser");
        
        // Mock users.getAllUsers() to return a list containing testUser
        when(users.getAllUsers()).thenReturn(List.of(testUser));

        // Call the getUser method
        ResponseEntity<User> response = userController.getUser("testUser");

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }


    @Test
    public void testGetUser_UserDoesNotExist() {
        // Mock users.getAllUsers() to return an empty list
        when(users.getAllUsers()).thenReturn(new ArrayList<>());

        // Call the getUser method with a username that does not exist
        ResponseEntity<User> response = userController.getUser("nonExistentUser");

        // Assert the response status
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Assert the response body is null (optional, but good for completeness)
        assertNull(response.getBody());
    }

    @Test
    public void testLogout() {
        userController.currentlyLoggedIn = testUser;

        ResponseEntity<User> response = userController.logout();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(userController.currentlyLoggedIn);
    }

    @Test
    public void testGetBasket_ReturnsBasket() {
        User currentlyLoggedIn = new User("testUser");
        currentlyLoggedIn.setPermissions(1); // Set a default permission level

        userController.currentlyLoggedIn = currentlyLoggedIn; // Set the logged-in user

        // Call the method
        ResponseEntity<Basket> response = userController.getBasket();

        // Assert that the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Check that the basket is not null (assuming it's initialized in User)
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetBasket_NoUserLoggedIn() {
        userController.currentlyLoggedIn = null;

        ResponseEntity<Basket> response = userController.getBasket();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testGetBasket_UserNotLoggedIn() {
        // Call the method when no user is logged in
        ResponseEntity<Basket> response = userController.getBasket();

        // Assert that the response status is BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void testGetHome_UserLoggedIn() {
        // Simulate a logged-in user by setting currentlyLoggedIn in the controller
        userController.currentlyLoggedIn = testUser;

        // Call the getHome() method of the controller directly
        ResponseEntity<User> response = userController.getHome();

        // Assert that the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // Assert that the body of the response is the correct user
        assertNotNull(response.getBody());
        assertEquals("testUser", response.getBody().getUsername());
    }
    @Test
    public void testGetHome_UserNotLoggedIn() {
        // Simulate that no user is logged in (currentlyLoggedIn is null)
        userController.currentlyLoggedIn = null;

        // Call the getHome() method of the controller directly
        ResponseEntity<User> response = userController.getHome();

        // Assert that the response status is BAD_REQUEST (400)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testAddToBasketSuccess() throws IOException {
        // Arrange
        User testUser = new User("testUser"); // Basket is auto-initialized
        userController.currentlyLoggedIn = testUser; // Simulate a logged-in user

        // Simulate the 'users' dependency
        Users mockUsers = Mockito.mock(Users.class);
        userController.users = mockUsers;

        Need testNeed = new Need(1, "Food", 10.0, 5, "Essential"); // Create a valid Need

        // Simulate adding the Need to the user's basket
        Mockito.doAnswer(invocation -> {
            testUser.getBasket().addNeed(new Need(
                testNeed.getId(), testNeed.getName(), testNeed.getCost(), 1, testNeed.getType()
            ));
            return null;
        }).when(mockUsers).addtoBasket(Mockito.any(User.class), Mockito.any(Need.class));

        // Act
        ResponseEntity<Basket> response = userController.addToBasket(testNeed);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status 200 OK");
        assertNotNull(response.getBody(), "Basket should not be null");
        assertEquals(1, response.getBody().getNeeds().size(), "Basket should contain one item");
        assertEquals("Food", response.getBody().getNeeds().iterator().next().getName(), "Need name should match");
    }
    @Test
    public void testAddToBasket_UserNotLoggedIn() {
        // Simulate that no user is logged in (currentlyLoggedIn is null)
        userController.currentlyLoggedIn = null;

        // Call the addToBasket() method of the controller directly with a Need object
        ResponseEntity<Basket> response = userController.addToBasket(testNeed);

        // Assert that the response status is BAD_REQUEST (400)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testAddToBasketIOException() throws IOException {
        // Arrange
        User testUser = new User("testUser"); // Create a User object
        userController.currentlyLoggedIn = testUser; // Simulate a logged-in user

        // Mock the Users dependency
        Users mockUsers = Mockito.mock(Users.class);
        userController.users = mockUsers;

        Need testNeed = new Need(1, "Food", 10.0, 5, "Essential"); // Create a valid Need

        // Simulate IOException when addtoBasket is called
        Mockito.doThrow(new IOException("Simulated IOException")).when(mockUsers).addtoBasket(Mockito.any(User.class), Mockito.any(Need.class));

        // Act
        ResponseEntity<Basket> response = userController.addToBasket(testNeed);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Expected status 500 Internal Server Error");
        assertNull(response.getBody(), "Response body should be null when an exception occurs");
    }
    @Test
    public void testAddFundsToBasket_success() throws IOException {
        // Arrange
        String amount = "100";
        int fundsToAdd = Integer.parseInt(amount);

        // Simulate that the user is logged in
        userController.currentlyLoggedIn = testUser;

        // Mock that the addFunds method works without issues
        doNothing().when(users).addFunds(testUser, fundsToAdd);

        // Act
        ResponseEntity<Basket> response = userController.addFundsToBasket(amount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status 200 OK");
        assertNotNull(response.getBody(), "Basket should not be null");
        // You can further check that the funds were added to the basket here
    }
    @Test
    public void testAddFundsToBasket_userNotLoggedIn() {
        // Simulate that no user is logged in (currentlyLoggedIn is null)
        userController.currentlyLoggedIn = null;

        // Act
        ResponseEntity<Basket> response = userController.addFundsToBasket("100");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected status 400 BAD_REQUEST");
    }
    @Test
    public void testAddFundsToBasket_invalidAmount() {
        // Simulate that a non-numeric string is provided for amount
        String invalidAmount = "invalidAmount";

        // Act
        ResponseEntity<Basket> response = userController.addFundsToBasket(invalidAmount);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected status 400 BAD_REQUEST for invalid amount");
    }
    @Test
    public void testAddFundsToBasket_internalServerError() throws IOException {
        // Arrange
        String amount = "100";
        int fundsToAdd = Integer.parseInt(amount);

        // Simulate that the user is logged in
        userController.currentlyLoggedIn = testUser;

        // Simulate that an IOException occurs when addFunds is called
        doThrow(new IOException("Simulated IOException")).when(users).addFunds(testUser, fundsToAdd);

        // Act
        ResponseEntity<Basket> response = userController.addFundsToBasket(amount);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Expected status 500 INTERNAL_SERVER_ERROR due to IOException");
    }
    @Test
    public void testGetFavorites_userLoggedIn() {
        // Arrange
        userController.currentlyLoggedIn = testUser; // Simulate a logged-in user

        // Act
        Need favoriteNeed1 = new Need(1, "Bone", 9.00, 10, "Dog");
        Need favoriteNeed2 = new Need(2, "Tennis Ball", 3.50, 6, "Toy");

        testUser.addFavorite(favoriteNeed1);
        testUser.addFavorite(favoriteNeed2);
        ResponseEntity<List<Need>> response = userController.getFavorites();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(2, response.getBody().size(), "Favorites list should contain 2 items");
        assertEquals("Bone", response.getBody().get(0).getName(), "First favorite should be 'Food'");
    }
    @Test
    public void testGetFavorites_userNotLoggedIn() {
        // Arrange
        userController.currentlyLoggedIn = null; // Simulate no logged-in user

        // Act
        ResponseEntity<List<Need>> response = userController.getFavorites();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected status 400 BAD_REQUEST when no user is logged in");
        assertNull(response.getBody(), "Response body should be null when no user is logged in");
    }
    @Test
    public void testAddFavorite_Success() throws IOException {
        // Arrange
        userController.currentlyLoggedIn = testUser; // Simulate a logged-in user
        Need newFavorite = new Need(3, "Shelter", 100.0, 1, "Essential");

        // Mock the behavior of the `users.addToFavorites` method
        doAnswer(invocation -> {
            testUser.getFavorites().add(newFavorite); // Simulate adding to favorites
            return null;
        }).when(users).addToFavorites(testUser, newFavorite);

        // Act
        ResponseEntity<List<Need>> response = userController.addFavorite(newFavorite);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(1, response.getBody().size(), "Favorites list should contain 3 items");
        assertTrue(response.getBody().contains(newFavorite), "New favorite should be in the favorites list");
    }
    @Test
    public void testAddFavorite_UserNotLoggedIn() {
        // Arrange
        userController.currentlyLoggedIn = null; // Simulate no logged-in user
        Need newFavorite = new Need(3, "Shelter", 100.0, 1, "Essential");

        // Act
        ResponseEntity<List<Need>> response = userController.addFavorite(newFavorite);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected status 400 BAD_REQUEST when no user is logged in");
        assertNull(response.getBody(), "Response body should be null when no user is logged in");
    }
    @Test
    public void testAddFavorite_IOException() throws IOException {
        // Arrange
        userController.currentlyLoggedIn = testUser; // Simulate a logged-in user
        Need newFavorite = new Need(3, "Shelter", 100.0, 1, "Essential");

        // Mock the behavior of the `users.addToFavorites` method to throw an IOException
        doThrow(new IOException("Simulated IOException")).when(users).addToFavorites(testUser, newFavorite);

        // Act
        ResponseEntity<List<Need>> response = userController.addFavorite(newFavorite);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Expected status 500 INTERNAL_SERVER_ERROR when an IOException occurs");
        assertNull(response.getBody(), "Response body should be null when an exception occurs");
    }
    @Test
    public void testRemoveFromFavorites_Success() throws IOException {
        // Arrange
        userController.currentlyLoggedIn = testUser; // Simulate a logged-in user
        Need existingFavorite = new Need(1, "Food", 10.0, 5, "Essential");
        Need anotherFavorite = new Need(2, "Clothing", 15.0, 2, "Non-Essential");

        // Add items to the user's favorites list
        testUser.addFavorite(existingFavorite);
        testUser.addFavorite(anotherFavorite);

        // Mock the behavior of the `users.removeFromFavorites` method
        doAnswer(invocation -> {
            testUser.getFavorites().remove(existingFavorite); // Simulate removal
            return null;
        }).when(users).removeFromFavorites(testUser, existingFavorite);

        // Act
        ResponseEntity<List<Need>> response = userController.removeFromFavorites(existingFavorite);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(1, response.getBody().size(), "Favorites list should contain 1 item");
        assertFalse(response.getBody().contains(existingFavorite), "Removed need should not be in the favorites list");
    }
    @Test
    public void testRemoveFromFavorites_UserNotLoggedIn() {
        // Arrange
        userController.currentlyLoggedIn = null; // Simulate no logged-in user
        Need existingFavorite = new Need(1, "Food", 10.0, 5, "Essential");

        // Act
        ResponseEntity<List<Need>> response = userController.removeFromFavorites(existingFavorite);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected status 400 BAD_REQUEST when no user is logged in");
        assertNull(response.getBody(), "Response body should be null when no user is logged in");
    }
    @Test
    public void testRemoveFromFavorites_IOException() throws IOException {
        // Arrange
        userController.currentlyLoggedIn = testUser; // Simulate a logged-in user
        Need existingFavorite = new Need(1, "Food", 10.0, 5, "Essential");
    
        // Add the item to the user's favorites list
        testUser.addFavorite(existingFavorite);
    
        // Mock the behavior of the `users.removeFromFavorites` method to throw an IOException
        doThrow(new IOException("Simulated IOException")).when(users).removeFromFavorites(testUser, existingFavorite);
    
        // Act
        ResponseEntity<List<Need>> response = userController.removeFromFavorites(existingFavorite);
    
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Expected status 500 INTERNAL_SERVER_ERROR when an IOException occurs");
        assertNull(response.getBody(), "Response body should be null when an exception occurs");
    }
    @Test
    public void testDeleteFromBasket_Success() throws IOException {
        // Arrange
        userController.currentlyLoggedIn = testUser; // Simulate a logged-in user
        Need needToRemove = new Need(1, "Food", 10.0, 5, "Essential");

        // Add the need to the user's basket
        testUser.getBasket().addNeed(needToRemove);

        // Mock the behavior of the `users.removeFromBasket` method
        doAnswer(invocation -> {
            testUser.getBasket().getNeeds().removeIf(need -> need.getId() == 1); // Simulate removal
            return null;
        }).when(users).removeFromBasket(testUser, 1);

        // Act
        ResponseEntity<Basket> response = userController.deleteFromBasket(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().getNeeds().isEmpty(), "Basket should be empty after removal");
    }
    @Test
    public void testDeleteFromBasket_UserNotLoggedIn() {
        // Arrange
        userController.currentlyLoggedIn = null; // Simulate no logged-in user
    
        // Act
        ResponseEntity<Basket> response = userController.deleteFromBasket(1);
    
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected status 400 BAD_REQUEST when no user is logged in");
        assertNull(response.getBody(), "Response body should be null when no user is logged in");
    }
    
    @Test
    public void testDeleteFromBasket_IOException() throws IOException {
        // Arrange
        userController.currentlyLoggedIn = testUser; // Simulate a logged-in user
        Need needToRemove = new Need(1, "Food", 10.0, 5, "Essential");
    
        // Add the need to the user's basket
        testUser.getBasket().addNeed(needToRemove);
    
        // Mock the behavior of the `users.removeFromBasket` method to throw an IOException
        doThrow(new IOException("Simulated IOException")).when(users).removeFromBasket(testUser, 1);
    
        // Act
        ResponseEntity<Basket> response = userController.deleteFromBasket(1);
    
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Expected status 500 INTERNAL_SERVER_ERROR when an IOException occurs");
        assertNull(response.getBody(), "Response body should be null when an exception occurs");
    }
}
