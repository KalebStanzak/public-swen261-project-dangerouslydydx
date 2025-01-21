package com.ufund.api.ufundapi.Users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.Need;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testUser");
        user.setUserID(0);
    }

    @Test
    public void testConstructor() {
        assertEquals("testUser", user.getUsername());
        assertEquals(0, user.getUserID());
    }

    @Test
    public void testGetUserID() {
        assertEquals(0, user.getUserID());
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testSetUserID() {
        user.setUserID(10);
        assertEquals(10, user.getUserID());
    }

    @Test
    public void testToString() {
        assertEquals("USERID: 0   USERNAME: testUser\n", user.toString());
    }

    @Test
    public void testEqualsSameUserID() {
        User anotherUser = new User("anotherUser");
        anotherUser.setUserID(0); // Match userID to test equality
        assertTrue(user.equals(anotherUser));
    }

    @Test
    public void testEqualsDifferentUserID() {
        User anotherUser = new User("anotherUser");
        anotherUser.setUserID(1);
        assertFalse(user.equals(anotherUser));
    }

    @Test
    public void testEqualsDifferentObject() {
        assertFalse(user.equals(new Object()));
    }
    @Test
    public void testSetPermissionsValid() {
        user.setPermissions(2);
        assertEquals(2, user.getPermissions());
    }
    @Test
    public void testSetPermissionsInvalidLow() {
        user.setPermissions(-1);
        assertEquals(1, user.getPermissions()); // Should remain unchanged
    }
    @Test
    public void testSetPermissionsInvalidHigh() {
        user.setPermissions(5);
        assertEquals(1, user.getPermissions()); // Should remain unchanged
    }
    @Test
    public void testAddFavorite() {
        Need need = new Need(); //Need default contructor
        user.addFavorite(need);
        List<Need> favorites = user.getFavorites();
        assertEquals(1, favorites.size());
        assertTrue(favorites.contains(need));
    }
    @Test
    public void testRemoveFavorite() {
        Need need = new Need(); //  Need Constructor
        user.addFavorite(need);
        user.removeFavorite(need);
        List<Need> favorites = user.getFavorites();
        assertEquals(0, favorites.size());
        assertFalse(favorites.contains(need));
    }
    @Test
    public void testInitialFavoritesEmpty() {
        assertTrue(user.getFavorites().isEmpty());
    }
}
