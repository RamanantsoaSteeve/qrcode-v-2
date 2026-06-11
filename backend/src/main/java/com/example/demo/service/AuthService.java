package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.AuthDto.UserLogin;
import com.example.demo.exception.LocalExceptionHandler;
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
    HashMap<String, Number> codes = new HashMap<>();

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final JavaMailSender mailSender;

    public Long registerUser(AuthDto.RegisterRequest dto) {
        if (authRepository.existsByEmail(dto.email())) {
            throw new LocalExceptionHandler("Email already exists");
        }

        AuthModel entity = authMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.password()));
        AuthModel savedEntity = authRepository.save(entity);

        return savedEntity.getId();
    }

    public String getToken(AuthDto.LoginRequest dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

            return tokenService.generateToken(authentication);
        } catch (AuthenticationException e) {
            throw new LocalExceptionHandler("Invalid email or password");
        }
    }

    public UserLogin GetUsetInfo(String email) {
        AuthModel authModel = authRepository.findByEmail(email)
                .orElseThrow(() -> new LocalExceptionHandler("Invalid email or password"));

        return authMapper.toDto(authModel);
    }

    public Long getIdByEmail(AuthDto.RequestEmail dto) {
        AuthModel authModel = authRepository.findByEmail(dto.email())
                .orElseThrow(() -> new LocalExceptionHandler("email introuvable"));
        return authModel.getId();
    }

    @Async
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
            throw new IllegalStateException("Échec de l'envoi de l'e-mail à " + to, e);
        }
    }

    public String generatHtml(Number code, String username, String to) {
        if (codes.containsKey(to)) {
            codes.remove(to);
        }

        codes.put(to, code);
        String htmlContent = """
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

        return htmlContent;
    }

    public void checkCode(Number code, String email) {
        if (!code.equals(codes.get(email))) {
            throw new LocalExceptionHandler("code invalid");

        }
    }

    public Number generateCode() {
        return Integer.valueOf(String.format("%04d", ThreadLocalRandom.current().nextInt(10000)));
    }
}