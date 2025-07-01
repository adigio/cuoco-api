package com.cuoco.adapter.out.mail;

import com.cuoco.application.usecase.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String token) {
        String confirmationLink = "http://localhost:8080/confirm?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmación de cuenta - Cuoco");
        message.setText("Gracias por registrarte en Cuoco.\n\n" +
                "Para confirmar tu cuenta, hacé clic en el siguiente enlace:\n" +
                confirmationLink + "\n\n" +
                "Este enlace es válido por 24 horas.");

        mailSender.send(message);
        log.info("Email de confirmación enviado a {}", to);
    }
}
