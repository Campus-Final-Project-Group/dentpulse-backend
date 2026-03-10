package com.dentpulse.dentalsystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("DentPulse - Email Verification Code");
            helper.setText(buildOtpEmailTemplate(otpCode), true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    private String buildOtpEmailTemplate(String otpCode) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>DentPulse Email Verification</title>
                </head>
                <body style="margin:0; padding:0; background-color:#0f1f17; font-family:Arial, sans-serif;">
                
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" 
                           style="background-color:#0f1f17; padding:30px 0;">
                        <tr>
                            <td align="center">
                
                                <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0"
                                       style="max-width:600px; background-color:#13271d; border-radius:16px; overflow:hidden; box-shadow:0 6px 20px rgba(0,0,0,0.25);">
                                    
                                    <tr>
                                        <td align="center" style="padding:35px 20px 20px 20px; background-color:#163222;">
                                            <h1 style="margin:0; font-size:38px; color:#2ecc71; font-weight:700;">DentPulse</h1>
                                            <p style="margin:10px 0 0 0; font-size:16px; color:#d6f5df;">
                                                Smart Dental Care Platform
                                            </p>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td style="padding:40px 35px; color:#ffffff;">
                                            <h2 style="margin:0 0 15px 0; font-size:30px; color:#2ecc71;">
                                                Email Verification
                                            </h2>
                
                                            <p style="margin:0 0 15px 0; font-size:17px; line-height:1.7; color:#e8f5ec;">
                                                Welcome to <strong>DentPulse</strong>!
                                            </p>
                
                                            <p style="margin:0 0 25px 0; font-size:16px; line-height:1.8; color:#dbeee1;">
                                                Use the verification code below to complete your registration.
                                                This code will expire in <strong>10 minutes</strong>.
                                            </p>
                
                                            <div style="text-align:center; margin:30px 0;">
                                                <div style="display:inline-block; background-color:#2ecc71; color:#0f1f17; 
                                                            font-size:34px; font-weight:bold; letter-spacing:10px; 
                                                            padding:18px 30px; border-radius:12px;">
                                                    %s
                                                </div>
                                            </div>
                
                                            <p style="margin:25px 0 0 0; font-size:14px; line-height:1.7; color:#b9d7c2;">
                                                If you did not request this verification, please ignore this email.
                                            </p>
                                        </td>
                                    </tr>
                
                                    <tr>
                                        <td align="center" style="padding:20px; border-top:1px solid #244533; color:#9fc3aa; font-size:13px;">
                                            © 2026 DentPulse. All rights reserved.
                                        </td>
                                    </tr>
                                </table>
                
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(otpCode);
    }
}