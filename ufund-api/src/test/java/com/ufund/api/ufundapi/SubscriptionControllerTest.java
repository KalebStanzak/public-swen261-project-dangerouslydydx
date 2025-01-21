package com.ufund.api.ufundapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SubscriptionControllerTest {
    @Test
    void testSubscribe_ValidEmail() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.email = "test@example.com";

        // Mock the behavior of the saveEmailToFile method (to avoid file I/O during tests)
        SubscriptionController subscriptionController = spy(new SubscriptionController());
        doNothing().when(subscriptionController).saveEmailToFile(anyString());

        // Act
        ResponseEntity<String> response = subscriptionController.subscribe(emailRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Subscription successful"));
        verify(subscriptionController, times(1)).saveEmailToFile("test@example.com");
    }
    @Test
    void testSubscribe_InvalidEmail() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.email = ""; // Empty email

        // Act
        ResponseEntity<String> response = new SubscriptionController().subscribe(emailRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid email"));
    }
    @Test
    void testSubscribe_NullEmail() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.email = null;

        // Act
        ResponseEntity<String> response = new SubscriptionController().subscribe(emailRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid email"));
    }
    @Test
    void testSaveEmailToFile() throws IOException {
        // Arrange
        String testEmail = "test@example.com";
        Path testFilePath = Paths.get("subscriptions.txt");

        // Ensure the test file is clean before we start
        if (Files.exists(testFilePath)) {
            Files.delete(testFilePath);
        }

        SubscriptionController subscriptionController = new SubscriptionController();

        // Act: Save email to file
        subscriptionController.saveEmailToFile(testEmail);

        // Assert: Verify that the email is written to the file
        assertTrue(Files.exists(testFilePath), "File should be created");
        assertTrue(Files.readAllLines(testFilePath).contains(testEmail), "The email should be in the file");

        // Clean up after test (delete the test file)
        Files.delete(testFilePath);
    }
}

