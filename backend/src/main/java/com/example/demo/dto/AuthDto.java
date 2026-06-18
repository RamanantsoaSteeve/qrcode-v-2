package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public class AuthDto {
        public record RegisterRequest(
                        @NotBlank(message = "Username Required") @Size(min = 5) String username,
                        @Email(message = "invalid email") @NotBlank(message = "Email Required") String email,
                        @NotBlank(message = "Username Required or too shorts") @Size(min = 6, message = "Password is too short") String password) {
        }

        @Builder
        public record ResponseRequestRegister(Long id, Boolean success) {
        }

        public record RequestEmail(String email) {
        }

        @Builder
        public record ResponseLoginFirebase(Boolean success, String token, Long id, String username) {
        }

        public record LoginRequest(
                        @Email(message = "Invalid email") @NotBlank(message = "champ is empty") String email,
                        @NotBlank(message = "Password is Blank") @Size(min = 6, message = "Password is too short") String password) {
        }

        public record UserLogin(String email, String username, Long id) {
        }

        @Builder
        public record ResponseRequestLogin(Boolean success, String token, UserLogin user) {
        }

        @Builder
        public record ResponseIdRegister(Boolean success, Long id) {
        }

        public record SendTextEmailDto(String text, String to, String subject, String username) {
        }

        public record RequestCodeDto(String email, Number code, Number id) {
        }

        @Builder
        public record ResponseCodeDto(String message, Boolean success, String token) {
        }
}