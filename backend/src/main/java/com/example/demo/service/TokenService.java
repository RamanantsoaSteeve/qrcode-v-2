package com.example.demo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthDto;
import com.example.demo.exception.LocalExceptionHandler;
import com.example.demo.model.AuthModel;
import com.example.demo.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final AuthRepository authRepository;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("mon-application-api")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String getToken(AuthDto.UserLogin dto) {
        try {
            AuthModel user = authRepository.findByEmail(dto.email())
                    .orElseThrow(() -> new LocalExceptionHandler("Email introuvable"));
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    null,
                    Collections.singletonList(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return generateToken(authentication);

        } catch (Exception e) {
            throw new LocalExceptionHandler("Erreur lors de l'authentification : " + e.getMessage());
        }
    }
}