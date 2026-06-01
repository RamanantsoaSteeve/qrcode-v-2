package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseAuthDto;
import com.example.demo.dto.ResponseAuthDto.ResponseRequestLogin;
import com.example.demo.dto.ResponseAuthDto.ResponseRequestRegister;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseRequestRegister> responseRegister(
            @RequestBody @Valid ResponseAuthDto.RegisterRequest dto) {
        return ResponseEntity.ok(new ResponseAuthDto.ResponseRequestRegister(authService.registerUser(dto), true));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseRequestLogin> postMethodName(@RequestBody @Valid ResponseAuthDto.LoginRequest dto) {
        String token = authService.getToken(dto);

        return ResponseEntity.ok(new ResponseRequestLogin(true, token, authService.GetUsetInfo(dto)));
    }

}
