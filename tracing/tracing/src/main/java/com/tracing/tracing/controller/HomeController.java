package com.tracing.tracing.controller;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeController {

    private final Tracer tracer;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public HomeController(Tracer tracer) {
        this.tracer = tracer;
    }
    @GetMapping("/trace")
    public String traceEndpoint() {
        // Start the parent span
        Span parentSpan = tracer.nextSpan().name("parent-span").start();
        try (var parentScope = tracer.withSpan(parentSpan)) {
            logger.info("Started parent span: {}", parentSpan.context().traceId());
            parentSpan.event("Log: Started processing parent span");
            // Simulate child operation 1
            createChildSpan("child-operation-1", 200);

            // Simulate child operation 2
            createChildSpan("child-operation-2", 300);

            // Parent span work
            Thread.sleep(100);
            logger.info("Finished parent span work");
            parentSpan.event("Log: Finished processing parent span");
            return "Tracing with multiple spans!";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error in parent span processing", e);
            return "Error!";
        } finally {
            parentSpan.end();
            logger.info("Parent span ended: {}", parentSpan.context().traceId());
        }
    }

    private void createChildSpan(String spanName, int simulatedWorkMillis) {
        // Start a child span
        Span childSpan = tracer.nextSpan().name(spanName).start();
        try (var childScope = tracer.withSpan(childSpan)) {
            logger.info("Started child span: {}", childSpan.context().spanId());
            childSpan.event("Log: Started " + spanName);
            // Simulate work
            Thread.sleep(simulatedWorkMillis);
            logger.info("Finished work in child span: {}", spanName);
            childSpan.event("Log: Ended " + spanName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error in child span: {}", spanName, e);
        } finally {
            childSpan.end();
            logger.info("Child span ended: {}", childSpan.context().spanId());
        }
    }


    @GetMapping("/trace2")
    public String traceEndpoinnt() {
        logger.info("Processing request in traceEndpoint");

        try {
            simulateWork("Task 1", 200);
            simulateWork("Task 2", 300);
            logger.info("Successfully completed request");
        } catch (InterruptedException e) {
            logger.error("Error while processing request", e);
            Thread.currentThread().interrupt();
            return "Error occurred";
        }

        return "Tracing and logging with Zipkin!";
    }

    private void simulateWork(String taskName, int delayMillis) throws InterruptedException {
        logger.info("Starting {}", taskName);
        Thread.sleep(delayMillis);
        logger.info("Finished {}", taskName);
    }


    @GetMapping("/trace3")
    public String traceEndpoint2() {
        // Start the parent span
        Span parentSpan = tracer.nextSpan().name("parent-span").start();
        try (var parentScope = tracer.withSpan(parentSpan)) {
            logger.info("Started parent span: {}", parentSpan.context().traceId());

            // Simulate child spans
            createChildSpan2("child-operation-1", 200);
            createChildSpan22("child-operation-22", 300);

            logger.info("Finished parent span work");
            return "Tracing with automatic log annotations!";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error in parent span processing", e);
            return "Error!";
        } finally {
            parentSpan.end();
        }
    }

    private void createChildSpan2(String spanName, int simulatedWorkMillis) throws InterruptedException {
        Span childSpan = tracer.nextSpan().name(spanName).start();
        try (var childScope = tracer.withSpan(childSpan)) {
            logger.info("Started child span: {}", childSpan.context().spanId());
            Thread.sleep(simulatedWorkMillis);
            logger.info("Completed work in child span: {}", spanName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error in child span: {}", spanName, e);
        } finally {
            childSpan.end();
        }
    }
    private void createChildSpan22(String spanName, int simulatedWorkMillis) throws InterruptedException {
        Span childSpan = tracer.nextSpan().name(spanName).start();
        try (var childScope = tracer.withSpan(childSpan)) {
            logger.info("Started child spannnnnnnnnnnnnnn: {}", childSpan.context().spanId());
            Thread.sleep(simulatedWorkMillis);
            logger.info("Completed work in child spannnnnnnnnnnnnnnn: {}", spanName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error in child span: {}", spanName, e);
        } finally {
            childSpan.end();
        }
    }









    @GetMapping("/path1")
    public ResponseEntity path1() {

        logger.info("Incoming request at {} for request /path1 ", "application-1");
        String response = restTemplate.getForObject("http://localhost:8095/path2", String.class);
        return ResponseEntity.ok("response from /path1 + " + response);
    }

    @GetMapping("/path2")
    public ResponseEntity path2() {
        logger.info("Incoming request at {} at /path2", "application-1");
        return ResponseEntity.ok("response from /path2 ");
    }
}
