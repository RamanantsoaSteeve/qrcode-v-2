package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ResponseAuthDto;
import com.example.demo.dto.ResponseAuthDto.UserLogin;
import com.example.demo.exception.LocalExceptionHandler;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.model.AuthModel;
import com.example.demo.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public Long registerUser(ResponseAuthDto.RegisterRequest dto) {
        if (authRepository.existsByEmail(dto.email())) {
            throw new LocalExceptionHandler("Email already exists");
        }

        AuthModel entity = authMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.password()));
        AuthModel savedEntity = authRepository.save(entity);

        return savedEntity.getId();
    }

    public String getToken(ResponseAuthDto.LoginRequest dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

            return tokenService.generateToken(authentication);
        } catch (AuthenticationException e) {
            throw new LocalExceptionHandler("Invalid email or password");
        }
    }

    public UserLogin GetUsetInfo(ResponseAuthDto.LoginRequest dto) {
        AuthModel authModel = authRepository.findByEmail(dto.email())
                .orElseThrow(() -> new LocalExceptionHandler("Invalid email or password"));

        return authMapper.toDto(authModel);
    }
}