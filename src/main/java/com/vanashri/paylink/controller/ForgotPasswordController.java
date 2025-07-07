package com.vanashri.paylink.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class ForgotPasswordController {

    @Autowired
    private JavaMailSender mailSender;

    // Dummy method: replace this with DB user fetch logic
    private boolean emailExists(String email) {
        // You should actually check from the DB if email exists
        return true;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        log.info("Forgot password request for email: {}", email);

        if (!emailExists(email)) {
            return ResponseEntity.badRequest().body("Email not found in system.");
        }

        // 1. Generate token
        String token = UUID.randomUUID().toString();

        // 2. Save token in DB (optional, for validation later)

        // 3. Create reset link (you can make this page in frontend)
        String resetLink = "http://localhost:8083/payment/reset-password?token=" + token;

        // 4. Send email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("vanashri7795@gmail.com"); // must match SMTP username
            message.setTo(email);
            message.setSubject("Reset Your Password");
            message.setText("To reset your password, click the link below:\n" + resetLink);

            mailSender.send(message);

            return ResponseEntity.ok("Password reset link sent to your email.");
        } catch (Exception e) {
            log.error("Failed to send email: ", e);
            return ResponseEntity.internalServerError().body("Error sending email.");
        }
    }
}
