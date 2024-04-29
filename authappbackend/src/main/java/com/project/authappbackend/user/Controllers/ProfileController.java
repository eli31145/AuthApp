package com.project.authappbackend.user.Controllers;

import com.project.authappbackend.user.User;
import com.project.authappbackend.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    UserRepository userRepository;

    /*authenticationManager bean is injected into Spring Security Config class, used to authenticate users during login process.
     Once user authenticated, Spring Security sets Authentication object in SecurityContext
     Here Authentication object is obtained as parameter, representing the currently authenticated user.*/
    @Autowired
    AuthenticationManager authenticationManager;

    @CrossOrigin
    @PatchMapping("/update")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody User updatedUser, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        String currentUserName = authentication.getName();
        Optional<User> optionalUser = userRepository.findByEmail(currentUserName);

        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();

            //Prevents updating of other's profile
            if (!currentUser.getUsername().equals(updatedUser.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own profile");
            }
            //When nothing is updated compared to before
            if (currentUser.equals(updatedUser)) {
                return ResponseEntity.badRequest().body("Updated profile details are the same as before");
            }

            //Update profile details
            currentUser.setEmail(updatedUser.getEmail());
            currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
            currentUser.setAddress(updatedUser.getAddress());

            LocalDateTime now = LocalDateTime.now();
            currentUser.setUpdatedDateTime(now);

            userRepository.save(currentUser);
            return ResponseEntity.ok("Profile updated Successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}