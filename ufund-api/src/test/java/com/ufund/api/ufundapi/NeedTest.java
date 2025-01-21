package com.ufund.api.ufundapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class NeedTest {
    private Need need;

    @BeforeEach
    void setUp() {
        need = new Need(0, "dog treat", 50.0, 10, "food");
    }

    @Test
    void testGetId() {
        assertEquals(0, need.getId());
    }

    @Test
    void testGetName() {
        assertEquals("dog treat", need.getName());
    }

    @Test
    void testSetName() {
        need.setName("cat treat");
        assertEquals("cat treat", need.getName());
    }

    @Test
    void testGetCost() {
        assertEquals(50.0, need.getCost());
    }

    @Test
    void testSetCost() {
        need.setCost(40.0);
        assertEquals(40.0, need.getCost());
    }

    @Test
    void testGetQuantity() {
        assertEquals(10, need.getQuantity());
    }

    @Test
    void testSetQuantity() {
        need.setQuantity(15);
        assertEquals(15, need.getQuantity());
    }

    @Test
    void testGetType() {
        assertEquals("food", need.getType());
    }

    @Test
    void testSetType() {
        need.setType("treat");
        assertEquals("treat", need.getType());
    }



}
