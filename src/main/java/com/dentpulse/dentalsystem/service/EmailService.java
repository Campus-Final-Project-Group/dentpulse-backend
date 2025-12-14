package com.dentpulse.dentalsystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/*@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("DentPulse - Email Verification Code");
        message.setText(
                "Your DentPulse verification code is: " + otpCode +
                        "\nThis code will expire in 10 minutes."
        );

        mailSender.send(message);
    }
}*/


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otpCode) {

        if (!emailEnabled) {
            System.out.println("DEV MODE OTP for " + toEmail + ": " + otpCode);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("DentPulse - Email Verification Code");
        message.setText(
                "Your DentPulse verification code is: " + otpCode +
                        "\nThis code will expire in 10 minutes."
        );

        mailSender.send(message);
    }
}

