package com.softwaremind.odolczykd.recruitment.auth.controller;

import com.softwaremind.odolczykd.recruitment.auth.rest.RestLoggedUser;
import com.softwaremind.odolczykd.recruitment.auth.rest.RestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.softwaremind.odolczykd.recruitment.config.security.SecurityConfig.USER_ROLE;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<RestLoggedUser> login(@RequestBody RestUser restUser) {
        var user = userDetailsManager.loadUserByUsername(restUser.username());
        if (user != null && passwordEncoder.matches(restUser.password(), user.getPassword())) {
            return ResponseEntity.ok(new RestLoggedUser(
                    user.getUsername(),
                    user.getAuthorities().iterator().next().getAuthority())
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RestUser restUser) {
        if (userDetailsManager.userExists(restUser.username())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User " + restUser.username() + " already exists");
        }

        var newUser = User.withUsername(restUser.username())
                .password(passwordEncoder.encode(restUser.password()))
                .roles(USER_ROLE)
                .build();
        userDetailsManager.createUser(newUser);

        return ResponseEntity.ok("User " + restUser.username() + " registered successfully");
    }
}
