package com.grocerycompare.backend.controller;

import com.grocerycompare.backend.DTO.UserDTO;
import com.grocerycompare.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        UserDTO userDTO = authService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateProfile(
            Authentication authentication,
            @RequestBody Map<String, String> payload) {

        String name = payload.get("name");
        UserDTO updatedUser = authService.updateProfile(authentication.getName(), name);
        return ResponseEntity.ok(updatedUser);
    }
}
