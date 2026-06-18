package com.example.demo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.AuthModel;
import com.example.demo.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

        private final JwtEncoder jwtEncoder;
        private final AuthRepository authRepository;
        private final PasswordEncoder passwordEncoder;

        public String generateToken(AuthModel user) {
                Instant now = Instant.now();

                List<String> roles = user.getEmail().endsWith("@admin.com") ? List.of("ROLE_ADMIN")
                                : List.of("ROLE_USER");

                JwtClaimsSet claims = JwtClaimsSet.builder()
                                .issuer("mon-application-api")
                                .issuedAt(now)
                                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                                .subject(user.getEmail())
                                .claim("userId", user.getId())
                                .claim("email", user.getEmail())
                                .claim("roles", roles)
                                .build();

                return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        }

        public String loginWithEmailAndPassword(AuthDto.LoginRequest dto) {
                AuthModel user = authRepository.findByEmail(dto.email())
                                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable."));
                if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
                        throw new BadCredentialsException("Mot de passe incorrect.");
                }

                return generateToken(user);
        }

        public String loginWithFirebase(String firebaseEmail, String name) {
                // Dans le flux Firebase, le frontend valide le token Google avec Firebase,
                // puis t'envoie l'email sécurisé.

                // 1. On cherche si l'utilisateur existe déjà dans ta BDD MySQL/PostgreSQL
                return authRepository.findByEmail(firebaseEmail)
                                .map(this::generateToken) // S'il existe, on génère son token direct
                                .orElseGet(() -> {
                                        // S'il n'existe pas, on le crée à la volée (Just-In-Time Registration)
                                        AuthModel newUser = new AuthModel();
                                        newUser.setEmail(firebaseEmail);
                                        newUser.setName(name);
                                        newUser.setPassword(passwordEncoder.encode(
                                                        "FIREBASE_EXTERNAL_ACCOUNT_" + Instant.now().toEpochMilli()));

                                        AuthModel savedUser = authRepository.save(newUser);
                                        return generateToken(savedUser);
                                });
        }
}