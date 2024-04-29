package com.project.authappbackend.user.Controllers;

import com.project.authappbackend.user.User;
import com.project.authappbackend.user.UserRepository;
import com.project.authappbackend.user.enums.UserStatus;
import com.project.authappbackend.user.models.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class AuthenticationController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        // Authenticate the user with the appropriate info stored in H2DB within authenticationManager.authenticate()
        /*
        *  Spring Security internally delegates the authentication process to an AuthenticationProvider,
        *  specifically DaoAuthenticationProvider in the case of username/password authentication.
        *  The DaoAuthenticationProvider retrieves the user details from the configured UserDetailsService,
        *  which in this case, would be an in-memory user details manager.
        *
        * */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String role = determineUserRole(request.getUsername());

        if (authentication.isAuthenticated()) {
            Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
            User authenticatedUser = optionalUser.get();

            LocalDateTime now = LocalDateTime.now();
            authenticatedUser.setLastLoginDateTime(now);
            authenticatedUser.setUserStatus(UserStatus.LOGGED_IN);

            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Logic to determine user role based on username
    private String determineUserRole(String username) {
        return username.equals("admin") ? "ADMIN" : "USER";
    }
}
