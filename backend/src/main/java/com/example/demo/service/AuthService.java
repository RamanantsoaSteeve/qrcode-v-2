package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.AuthDto.UserLogin;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.model.AuthModel;
import com.example.demo.repository.AuthRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final StringRedisTemplate redisTemplate;
    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JavaMailSender mailSender;

    public Long registerUser(AuthDto.RegisterRequest dto) {
        if (authRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already exists");
        }

        AuthModel entity = authMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.password()));
        AuthModel savedEntity = authRepository.save(entity);

        return savedEntity.getId();
    }

    public String getToken(AuthDto.LoginRequest dto) {
        try {
            return tokenService.loginWithEmailAndPassword(dto);
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid email or password");
        }
    }

    public UserLogin getUserInfo(String email) {
        AuthModel authModel = authRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        return authMapper.toDto(authModel);
    }

    public AuthModel getUserAllInfo(String email) {
        return authRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));
    }

    public Long getIdByEmail(String email) {
        AuthModel authModel = authRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email introuvable"));

        return authModel.getId();
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("ramanantsoasteeve@gmail.com");
            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new BadRequestException("Échec de l'envoi de l'e-mail à " + to + " (Erreur technique)");
        }
    }

    public String generatHtml(Number code, String username, String to) {
        redisTemplate.opsForValue().set(to, String.valueOf(code), 5, TimeUnit.MINUTES);
        System.out.println("Voila     " + redisTemplate.opsForValue().get(to));

        System.out.println(code);

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                        .card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                        .btn { background: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <h1>Bienvenue, %s !</h1>
                        <p>Merci d'avoir rejoint notre application de gestion de QR Codes.</p>
                        <p>Cliquez sur le bouton ci-dessous pour activer votre compte :</p>
                        <br>
                        <h1>%s</h1>
                    </div>
                </body>
                </html>
                """
                .formatted(username, code);
    }

    public void checkCode(Number code, String email) {
        String savedCode = redisTemplate.opsForValue().get(email);
        if (savedCode == null) {
            throw new BadRequestException("Le code a expiré ou n'existe pas");
        }

        if (!savedCode.equals(String.valueOf(code))) {
            throw new BadRequestException("Code invalide");
        }

        redisTemplate.delete(email);
    }

    public Number generateCode() {
        return Integer.valueOf(String.format("%04d", ThreadLocalRandom.current().nextInt(10000)));
    }

    public void changePassword(AuthDto.ChangePasswordRequest dto) {
        AuthModel authModel = authRepository.findById(dto.id()).orElseThrow();
        authModel.setPassword(passwordEncoder.encode(dto.password()));
    }
}