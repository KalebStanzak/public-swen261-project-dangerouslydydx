package com.ufund.api.ufundapi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.Users.UserController;

@ExtendWith(MockitoExtension.class)
public class BasketControllerTest {

    @InjectMocks
    private BasketController basketController;

    @Mock
    private NeedDAO needFileDAO;

    @Mock
    private UserController userController;

    private Basket mockBasket;

    @BeforeEach
    public void setup() {
        mockBasket = new Basket();
    }

    @Test
    void testCheckout_EmptyBasket() throws IOException {
        // Arrange
        when(userController.getBasket()).thenReturn(ResponseEntity.ok(mockBasket));

        // Act
        ResponseEntity<String> response = basketController.checkout();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Basket is empty. Nothing to checkout.", response.getBody());
    }

    @Test
    void testCheckout_InsufficientQuantity() throws IOException {
        // Arrange
        Need need = new Need(1, "Test Need", 10.0, 5, "test");
        mockBasket.addNeed(need);

        Need cupboardNeed = new Need(1, "Test Need", 10.0, 3, "test");
        when(userController.getBasket()).thenReturn(ResponseEntity.ok(mockBasket));
        when(needFileDAO.getNeed(1)).thenReturn(cupboardNeed);

        // Act
        ResponseEntity<String> response = basketController.checkout();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Insufficient quantity for item: Test Need", response.getBody());
    }

    @Test
    void testCheckout_Success() throws IOException {
        // Arrange
        Need basketNeed = new Need(1, "Test Need", 10.0, 2, "test");
        mockBasket.addNeed(basketNeed);
    
        Need cupboardNeed = new Need(1, "Test Need", 10.0, 5, "test");
        when(userController.getBasket()).thenReturn(ResponseEntity.ok(mockBasket));
        when(needFileDAO.getNeed(1)).thenReturn(cupboardNeed);
    
        // Act
        ResponseEntity<String> response = basketController.checkout();
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Checkout successful! Total amount donated: $20.00"));
    
        // Verify cupboard quantities updated
        verify(needFileDAO, times(2)).getNeed(1); // Expect 2 calls (validation + update)
    
        assertEquals(3, cupboardNeed.getQuantity()); // 5 - 2 = 3
        assertTrue(mockBasket.getNeeds().isEmpty());
    }
    @Test
    void testCheckout_BasketCleared() throws IOException {
        // Arrange
        Need need = new Need(1, "Test Need", 10.0, 2, "test");
        mockBasket.addNeed(need);

        Need cupboardNeed = new Need(1, "Test Need", 10.0, 5, "test");
        when(userController.getBasket()).thenReturn(ResponseEntity.ok(mockBasket));
        when(needFileDAO.getNeed(1)).thenReturn(cupboardNeed);

        // Act
        basketController.checkout();

        // Assert
        assertTrue(mockBasket.getNeeds().isEmpty());
    }
}
