package com.example.demo.controller;

import com.example.demo.service.TokenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.AuthDto.ResponseCodeDto;
import com.example.demo.dto.AuthDto.ResponseIdRegister;
import com.example.demo.dto.AuthDto.ResponseRequestLogin;
import com.example.demo.dto.AuthDto.ResponseRequestRegister;
import com.example.demo.dto.AuthDto.UserLogin;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final TokenService tokenService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseRequestRegister> responseRegister(@RequestBody @Valid AuthDto.RegisterRequest dto) {
        return ResponseEntity.ok(AuthDto.ResponseRequestRegister.builder()
                .id(authService.registerUser(dto))
                .success(true)
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseRequestLogin> postMethodName(@RequestBody @Valid AuthDto.LoginRequest dto) {
        return ResponseEntity.ok(AuthDto.ResponseRequestLogin.builder()
                .success(true)
                .token(authService.getToken(dto))
                .user(authService.GetUsetInfo(dto.email()))
                .build());
    }

    @PostMapping("/check-user")
    public ResponseEntity<ResponseIdRegister> postMethodName(@RequestBody String email) {
        return ResponseEntity.ok(AuthDto.ResponseIdRegister.builder()
                .success(true)
                .id(authService.getIdLong(email))
                .build());
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendTextEmail(@RequestBody @Valid AuthDto.SendTextEmailDto dto) {
        authService.sendHtmlEmail(dto.to(), dto.subject(),
                authService.generatHtml(dto.text(), dto.username(), dto.to()));

        return ResponseEntity.ok("email send with success");
    }

    @PostMapping("/send-code")
    public ResponseEntity<ResponseCodeDto> getAndCheckCode(@RequestBody @Valid AuthDto.RequestCodeDto dto) {
        authService.checkCode(dto.code(), dto.email());
        UserLogin userLogin = authService.GetUsetInfo(dto.email());

        return ResponseEntity.ok(AuthDto.ResponseCodeDto.builder()
                .token(tokenService.getToken(userLogin))
                .success(true)
                .message("code valid")
                .build());
    }

}
