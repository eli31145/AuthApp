package com.project.authappbackend.user.Controllers;

import com.project.authappbackend.user.User;
import com.project.authappbackend.user.UserRepository;
import com.project.authappbackend.user.Utils;
import com.project.authappbackend.user.models.ChangePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/password")
public class PasswordController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestParam String username, @RequestParam String email) {

        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(username, email);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User userToResetPassword = optionalUser.get();

        String randomPassword = Utils.generateRandomPassword();
        userToResetPassword.setPassword(Utils.encodePassword(randomPassword));

        LocalDateTime now = LocalDateTime.now();
        userToResetPassword.setUpdatedDateTime(now);
        userRepository.save(userToResetPassword);

        Utils.deliverPasswordViaMockService(userToResetPassword.getEmail(), randomPassword);

        return ResponseEntity.ok("Password reset successfully. Password sent to terminal.");
    }

    @CrossOrigin
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/change")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        String name = changePasswordRequest.getUser();
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        Optional<User> optionalUser = userRepository.findByUsername(name);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        //Checks if old password passed over is same as the one saved DB
        if (!Utils.passwordsMatch(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect.");
        }
        //Checks if passwords are the same
        if (oldPassword.equals(newPassword)) {
            return ResponseEntity.badRequest().body("New password must be different from old password.");
        }

        user.setPassword(Utils.encodePassword(newPassword));

        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedDateTime(now);
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully and saved");
    }

}
