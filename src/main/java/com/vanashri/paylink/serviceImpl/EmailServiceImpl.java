package com.vanashri.paylink.serviceImpl;

import com.vanashri.paylink.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendResetPasswordEmail(String to, String token) {
        try {
            String resetLink = "http://localhost:8083/payment/reset-password?token=" + token;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Reset Your Password");
            helper.setText(
                    "<p>Hello,</p>" +
                            "<p>You requested to reset your password. Click the link below:</p>" +
                            "<a href=\"" + resetLink + "\">Reset Password</a>", true);

            mailSender.send(message);
            log.info("Reset password email sent to: {}", to);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
