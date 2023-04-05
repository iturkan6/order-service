package com.example.orderservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@SecurityRequirement(name = "BasicAuth")
@RequiredArgsConstructor
public class TestController {
    @GetMapping
    public ResponseEntity<String> gettingTest(){
        return ResponseEntity.ok("Some response");
    }
}
