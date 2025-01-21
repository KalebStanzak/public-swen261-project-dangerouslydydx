package com.ufund.api.ufundapi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/subscribe")
public class SubscriptionController {

    @PostMapping
    public ResponseEntity<String> subscribe(@RequestBody EmailRequest emailRequest) {
        if (emailRequest.email == null || emailRequest.email.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\": \"Invalid email\"}");
        }

        saveEmailToFile(emailRequest.email);
        // need to send as a JSON
        String jsonResponse = "{\"message\": \"Subscription successful\"}";
        return ResponseEntity.ok(jsonResponse);
    }

    void saveEmailToFile(String email) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("subscriptions.txt", true))) {
            writer.write(email);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class EmailRequest {
    public String email;
}
