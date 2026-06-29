package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public class AuthDto {

        @Builder
        public record ResponseRequestRegister(Long id, Boolean success) {
        }

        @Builder
        public record ResponseToken(String token) {
        }

        @Builder
        public record ResponseLoginFirebase(Boolean success, String token, Long id, String username) {
        }

        public record RequestEmail(String email) {
        }

        @Builder
        public record ResponseCodeSendWithEmail(String message) {
        }

        @Builder
        public record ResponseStandard(boolean success, String message) {
        }

        @Builder
        public record ResponseRequestLogin(Boolean success, String token, UserLogin user) {
        }

        @Builder
        public record ResponseIdRegister(Boolean success, Long id) {
        }

        @Builder
        public record ResponseCodeDto(Boolean success, String token, Long id) {
        }

        public record LoginRequest(
                        @Email(message = "Invalid email") @NotBlank(message = "champ is empty") String email,
                        @NotBlank(message = "Password is Blank") @Size(min = 6, message = "Password is too short") String password) {
        }

        public record RegisterRequest(
                        @NotBlank(message = "Username Required") @Size(min = 5) String username,
                        @Email(message = "invalid email") @NotBlank(message = "Email Required") String email,
                        @NotBlank(message = "Username Required or too shorts") @Size(min = 6, message = "Password is too short") String password) {
        }

        public record UserLogin(String email, String username, Long id) {
        }

        public record SendTextEmailDto(String text, String to, String subject, String username) {
        }

        public record RequestCodeDto(RegisterRequest registerRequest, Number code) {
        }

        public record ChangePasswordRequest(String password, Long id) {
        }

        public record RequestCodeCheck(Number code, String email) {
        }
}