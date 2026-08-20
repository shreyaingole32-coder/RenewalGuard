package com.renewalguard.controller;

import com.renewalguard.domain.entity.User;
import com.renewalguard.dto.AuthResponse;
import com.renewalguard.dto.LoginRequest;
import com.renewalguard.repository.UserRepository;
import com.renewalguard.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid credentials"
                        ));

        if (!user.isActive() ||
                user.getPasswordHash() == null ||
                !passwordEncoder.matches(
                        request.password(),
                        user.getPasswordHash()
                )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }

        String token = jwtService.generateToken(
                user.getId().toString(),
                user.getRole().name()
        );

        return new AuthResponse(
                token,
                token,
                3600
        );
    }
}
