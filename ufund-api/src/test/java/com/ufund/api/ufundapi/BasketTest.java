package com.ufund.api.ufundapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

class BasketTest {
    private Basket basket;

    @BeforeEach
    void setUp() {
        basket = new Basket(1, "Test Basket", 100.0);
    }

    @Test
    void testGetId() {
        assertEquals(1, basket.getId());
    }

    @Test
    void testSetId() {
        basket.setId(2);
        assertEquals(2, basket.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Test Basket", basket.getName());
    }

    @Test
    void testSetName() {
        basket.setName("New Basket");
        assertEquals("New Basket", basket.getName());
    }

    @Test
    void testGetFunds() {
        assertEquals(100.0, basket.getFunds());
    }

    @Test
    void testSetFunds() {
        basket.setFunds(200.0);
        assertEquals(200.0, basket.getFunds());
    }

    @Test
    void testGetNeeds() {
        Set<Need> needs = basket.getNeeds();
        assertNotNull(needs);
        assertTrue(needs.isEmpty());
    }

    @Test
    void testAddNeed() {
        // Arrange
        Need need = new Need(1, "Food", 10.0, 6, "Dog");

        // Act
        basket.addNeed(need);

        // Assert
        assertEquals(1, basket.getNeeds().size());
        assertTrue(basket.getNeeds().contains(need)); // Validate the Need object was added
    }

    @Test
    void testRemoveNeed() {
        // Arrange
        Need need = new Need(1, "Bedding", 5.0, 3, "Cat");
        basket.addNeed(need);

        // Act
        basket.removeNeed(1); // Pass the ID of the need to remove

        // Assert
        assertTrue(basket.getNeeds().isEmpty());
    }

    @Test
    void testCalculateTotalCost() {
        Need need1 = new Need(1, "Food", 30.0, 6, "Fish");
        Need need2 = new Need(2, "Toys", 3.5, 10, "Tennis Ball");
        basket.addNeed(need1);
        basket.addNeed(need2);
        assertEquals(33.5, basket.calculateTotalCost());
    }
}
