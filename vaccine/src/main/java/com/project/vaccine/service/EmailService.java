package com.project.vaccine.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendVerificationEmail(String to, String subject, String code, String verificationMethod) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true để gửi email HTML
            helper.setTo(to);
            helper.setSubject(subject);

            String method = switch (verificationMethod) {
                case "REGISTER" -> "register";
                case "RESET_PASSWORD" -> "reset password";
                default -> "verification";
            };

            // Load template HTML with Thymeleaf
            Context context = new Context();
            context.setVariable("method", method);
            context.setVariable("code", code);
            String htmlContent = templateEngine.process("email-template", context);

            helper.setText(htmlContent, true); // true để gửi email dạng HTML
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
