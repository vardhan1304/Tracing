package com.tracing.elastic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/")
    public String hello() {
        logger.info("Handling request to /");

        // Simulate an HTTP request to trigger some operations
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/1", String.class);

        // Log the response
        logger.debug("Received response: {}", response);

        return "Hello from OpenTelemetry Spring Boot!";
    }
}
