package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.AppUser;
import com.example.inventorymanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        try {
            Authentication authentication = authService.authenticateUser(username, password);
            return ResponseEntity.ok("AppUser " + authentication.getName() + " logged in successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logoutUser();
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUser appUser) {
        Optional<AppUser> registeredUser = authService.registerUser(appUser.getUsername(), appUser.getPassword(), appUser.getRoles());
        if (registeredUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser.get());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
    }
}
