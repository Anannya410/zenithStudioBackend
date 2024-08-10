package com.codebrewers.backend.service;

import com.codebrewers.backend.dao.User;
import com.codebrewers.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Registration method
    public User registerUser(User user) throws Exception {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new Exception("Username already taken");
        }

        // Save the user with the plain text password
        return userRepository.save(user);
    }

    // Login method
    public User loginUser(String username, String password) throws Exception {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            if (password.equals(existingUser.get().getPassword())) {
                return existingUser.get();
            } else {
                throw new Exception("Invalid password");
            }
        } else {
            throw new Exception("User not found");
        }
    }


}
