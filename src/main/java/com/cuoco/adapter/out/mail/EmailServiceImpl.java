package com.cuoco.adapter.out.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmationEmail(String to, String confirmationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("latribudemicalle1480@gmail.com");
            helper.setTo(to);
            helper.setSubject("Confirma tu cuenta en Cuoco");

            String content = """
                <html>
                    <body>
                        <h2>¡Bienvenido a Cuoco!</h2>
                        <p>Por favor, confirma tu cuenta haciendo clic en el siguiente enlace:</p>
                        <a href="%s">Confirmar cuenta</a>
                        <p>Si no creaste una cuenta en Cuoco, puedes ignorar este mensaje.</p>
                    </body>
                </html>
                """.formatted(confirmationLink);

            helper.setText(content, true);

            mailSender.send(message);
            log.info("Correo de confirmación enviado a: {}", to);
        } catch (MessagingException e) {
            log.error("Error al enviar correo de confirmación a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error al enviar correo de confirmación", e);
        }
    }
}

