package com.gerardo.churchmanager.backend.auth.service;

import com.gerardo.churchmanager.backend.auth.dto.AuthResponse;
import com.gerardo.churchmanager.backend.auth.dto.LoginRequest;
import com.gerardo.churchmanager.backend.auth.dto.RegisterRequest;
import com.gerardo.churchmanager.backend.user.entity.User;
import com.gerardo.churchmanager.backend.user.enums.Role;
import com.gerardo.churchmanager.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException("Email already exists");

        }

        if (userRepository.existsByUsername(request.getUsername())) {

            throw new RuntimeException("Username already exists");

        }

        User user = User.builder()

                .username(request.getUsername())

                .email(request.getEmail())

                .password(

                        passwordEncoder.encode(

                                request.getPassword()

                        )

                )

                .role(Role.USER)

                .build();

        userRepository.save(user);

    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}