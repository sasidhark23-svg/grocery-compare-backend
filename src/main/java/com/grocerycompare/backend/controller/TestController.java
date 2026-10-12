package com.grocerycompare.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/health")
    public java.util.Map<String, String> health() {
        return java.util.Map.of("status", "UP");
    }

    @GetMapping("/api/test")
    public String testBackend() {
        return "GroceryCompare Backend is Working!";
    }
}
