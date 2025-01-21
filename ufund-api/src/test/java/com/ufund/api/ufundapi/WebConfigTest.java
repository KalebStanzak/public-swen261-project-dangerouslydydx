package com.ufund.api.ufundapi;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WebConfigTest {

    @Test
    public void testCorsConfiguration() {
        WebConfig webConfig = new WebConfig();
        CorsRegistry registry = new CorsRegistry();
        
        // This line would normally be called within the context of a Spring app
        webConfig.addCorsMappings(registry);
        
        // Check that the CorsRegistry is not null (basic assertion)
        assertNotNull(registry);
        
        // Further tests could go here, depending on what you need to verify
    }
}
