package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.AppUser;
import com.example.inventorymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<AppUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public AppUser saveUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public AppUser updateUser(Long id, AppUser userDetails) {
        return userRepository.findById(id)
                .map(appUser -> {
                    appUser.setUsername(userDetails.getUsername());
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        appUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }
                    appUser.setRoles(userDetails.getRoles());
                    return userRepository.save(appUser);
                }).orElseThrow(() -> new RuntimeException("AppUser not found with id " + id));
    }
}
