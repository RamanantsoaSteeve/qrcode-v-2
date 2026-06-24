package com.example.demo.controller;

import com.example.demo.service.TokenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.AuthDto.ResponseCodeDto;
import com.example.demo.dto.AuthDto.ResponseCodeSendWithEmail;
import com.example.demo.dto.AuthDto.ResponseIdRegister;
import com.example.demo.dto.AuthDto.ResponseStandard;
import com.example.demo.dto.AuthDto.ResponseToken;
import com.example.demo.dto.AuthDto.ResponseRequestLogin;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final TokenService tokenService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseRequestLogin> Login(@RequestBody @Valid AuthDto.LoginRequest dto) {
        return ResponseEntity.ok(AuthDto.ResponseRequestLogin.builder()
                .success(true)
                .token(authService.getToken(dto))
                .user(authService.getUserInfo(dto.email()))
                .build());
    }

    @PostMapping("/check-user")
    @PreAuthorize("#dto.email() == authentication.principal.claims['email'].toString()")
    public ResponseEntity<ResponseIdRegister> CheckUser(@RequestBody @Valid AuthDto.RequestEmail dto) {

        return ResponseEntity.ok(AuthDto.ResponseIdRegister.builder()
                .success(true)
                .id(authService.getIdByEmail(dto.email()))
                .build());
    }

    @PostMapping("/send-email")
    public ResponseEntity<ResponseCodeSendWithEmail> sendTextEmail(@RequestBody @Valid AuthDto.SendTextEmailDto dto) {
        authService.sendHtmlEmail(dto.to(), dto.subject(),
                authService.generatHtml(authService.generateCode(), dto.username(), dto.to()));

        return ResponseEntity.ok(AuthDto.ResponseCodeSendWithEmail.builder()
                .message("email send with success")
                .build());
    }

    @PostMapping("/get-code")
    public ResponseEntity<ResponseCodeDto> getAndCheckCodeRegister(@RequestBody @Valid AuthDto.RequestCodeDto dto) {
        authService.checkCode(dto.code(), dto.registerRequest().email());
        Long id = authService.registerUser(dto.registerRequest());

        return ResponseEntity.ok(AuthDto.ResponseCodeDto.builder()
                .token(tokenService.generateToken(authService.getUserAllInfo(dto.registerRequest().email())))
                .success(true)
                .id(id)
                .message("code valid")
                .build());
    }

    @PostMapping("/get-code-forgot")
    public ResponseEntity<ResponseToken> checkCodePasswordForgot(@RequestBody @Valid AuthDto.RequestCodeCheck dto) {
        authService.checkCode(dto.code(), dto.email());

        return ResponseEntity.ok(AuthDto.ResponseToken.builder()
                .token(tokenService.generateToken(authService.getUserAllInfo(dto.email())))
                .build());
    }

    @PostMapping("/change-password-user")
    @PreAuthorize("#dto.id.toString() == authentication.principal.claims['userId'].toString()")
    public ResponseEntity<ResponseStandard> changePassword(@RequestBody AuthDto.ChangePasswordRequest dto) {
        authService.changePassword(dto);

        return ResponseEntity.ok(AuthDto.ResponseStandard
                .builder()
                .message("password change with success")
                .build());
    }
}
