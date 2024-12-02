package com.elkspringboot.controller;


import com.elkspringboot.modal.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Slf4j
public class UserController {

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable String id){
        Long userId;
        try {
            userId = Long.parseLong(id);

        }catch (Exception e){
            String message = String.format("Invalid user id : %s. Please enter a valid numeric value.", id);
            log.error(message);
            return ResponseEntity.badRequest().body(message);
        }
        log.info("Request with the id = [{}]",userId);
        final User user = User.builder()
                .id(userId)
                .dateOfBirth(LocalDateTime.now())
                .name("name_"+userId)
                .build();
        log.info("Data retrieved with the id = [{}]",userId);
        return ResponseEntity.ok().body(user);
    }
}
