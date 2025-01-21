package com.ufund.api.ufundapi.Users;

import com.ufund.api.ufundapi.Basket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("testUser");
        testUser.setUserID(1);
        testUser.setPermissions(3);
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        when(users.getUser(testUser.getUserID())).thenReturn(testUser);

        ResponseEntity<User> response = userController.createUser("testUser");

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUser_Success() throws IOException {
        when(users.getUser(testUser.getUserID())).thenReturn(null);

        ResponseEntity<User> response = userController.createUser("testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(users).createUser("testUser");
    }

    @Test
    public void testCreateUser_InternalServerError() throws IOException {
        when(users.getUser(testUser.getUserID())).thenReturn(null);
        doThrow(new IOException()).when(users).createUser("testUser");

        ResponseEntity<User> response = userController.createUser("testUser");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUser_UserExists() {
        when(users.getAllUsers()).thenReturn(List.of(testUser));

        ResponseEntity<User> response = userController.getUser("testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    public void testGetUser_UserDoesNotExist() {
        when(users.getAllUsers()).thenReturn(new ArrayList<>());

        ResponseEntity<User> response = userController.getUser("nonExistentUser");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetAllUsers_AuthorizedUser() {
        userController.currentlyLoggedIn = testUser;
        when(users.getAllUsers()).thenReturn(List.of(testUser));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetAllUsers_NoUserLoggedIn() {
        // Simulate no user logged in
        userController.currentlyLoggedIn = null;

        // Call the method
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert that the response is UNAUTHORIZED
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testLogout() {
        userController.currentlyLoggedIn = testUser;

        ResponseEntity<User> response = userController.logout();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(userController.currentlyLoggedIn);
    }

    // @Test
    // public void testChangeUser_Authorized() throws IOException {
    //     userController.currentlyLoggedIn = testUser;

    //     ResponseEntity<User> response = userController.changeUser(testUser);

    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     verify(users).changeUser(testUser);
    // }
    // @Test
    // public void testChangeUser_InternalServerError() throws IOException {
    //     // Simulate a logged-in user with sufficient permissions
    //     User authorizedUser = new User("authorizedUser");
    //     authorizedUser.setPermissions(3); // Set permissions to 3 or greater
    //     userController.currentlyLoggedIn = authorizedUser;

    //     // Create a user object to pass to the method
    //     User userToUpdate = new User("userToUpdate");

    //     // Simulate IOException when changing user
    //     doThrow(new IOException()).when(users).changeUser(userToUpdate);

    //     // Call the method
    //     ResponseEntity<User> response = userController.changeUser(userToUpdate);

    //     // Assert that the response is INTERNAL_SERVER_ERROR
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    // }
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
}
