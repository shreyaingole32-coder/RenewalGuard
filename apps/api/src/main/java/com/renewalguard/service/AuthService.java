
package com.renewalguard.service;

import com.renewalguard.domain.entity.User;
import com.renewalguard.dto.AuthResponse;
import com.renewalguard.dto.LoginRequest;
import com.renewalguard.repository.UserRepository;
import com.renewalguard.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final long TOKEN_EXPIRATION_SECONDS = 3600;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid credentials"
                        )
                );

        if (!user.isActive()) {
            throw new IllegalArgumentException(
                    "User account is inactive"
            );
        }

        if (user.getPasswordHash() == null ||
                !passwordEncoder.matches(
                        request.password(),
                        user.getPasswordHash())) {

            throw new IllegalArgumentException(
                    "Invalid credentials"
            );
        }

        String token = jwtService.generateToken(
                user.getId().toString(),
                user.getOrganization().getId().toString(),
                user.getRole().name()
        );

        return new AuthResponse(
                token,
                token,
                TOKEN_EXPIRATION_SECONDS
        );
    }
}
