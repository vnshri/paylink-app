package com.vanashri.paylink.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendResetPasswordEmail(String to, String token);

}
