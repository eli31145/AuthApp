package com.project.authappbackend.user.Controllers;

import com.project.authappbackend.user.User;
import com.project.authappbackend.user.UserRepository;
import com.project.authappbackend.user.Utils;
import com.project.authappbackend.user.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @CrossOrigin
    @PostMapping("/onboard")
    public ResponseEntity<String> onboardUser(@RequestBody User newUser) {
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("User already exists!");
        }

        String randomPassword = Utils.generateRandomPassword();
        newUser.setPassword(Utils.encodePassword(randomPassword));

        LocalDateTime now = LocalDateTime.now();
        newUser.setCreatedDateTime(now);

        newUser.setRole(Role.USER);

        userRepository.save(newUser);

        Utils.deliverPasswordViaMockService(newUser.getEmail(), randomPassword);
        return ResponseEntity.ok("User onboarded successfully. Password sent to terminal.");
    }

}
