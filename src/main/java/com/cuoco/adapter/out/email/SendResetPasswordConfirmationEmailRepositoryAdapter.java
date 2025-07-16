package com.cuoco.adapter.out.email;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.application.port.out.SendResetPasswordConfirmationRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.model.ErrorDescription;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendResetPasswordConfirmationEmailRepositoryAdapter implements SendResetPasswordConfirmationRepository {

    private final String EMAIL_BODY = FileReader.execute("email/resetPassword.html");

    @Value("${shared.email.no-reply.name}")
    private String fromName;

    @Value("${shared.email.no-reply.from}")
    private String fromEmail;

    private final HttpServletRequest request;
    private final JavaMailSender mailSender;

    @Override
    public void execute(User user, String token) {
        try {
            log.info("Executing send reset password email for user with ID {}", user.getId());

            String passwordLink = buildPasswordLink(token);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress(fromEmail, fromName));
            helper.setTo(user.getEmail());
            helper.setSubject("Solicitud de cambio de contraseña");

            String content = EMAIL_BODY
                    .replace("{{NAME}}", user.getName())
                    .replace("{{LINK}}", passwordLink);

            helper.setText(content, true);

            mailSender.send(message);

            log.info("Successfully sended confirmation email to {} with link {}", user.getEmail(), passwordLink);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending confirmation email to {}: {}", user.getEmail(), e.getMessage());
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }

    private String buildPasswordLink(String token) {

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");

        return baseUrl
                .concat("/reset-password?token=")
                .concat(token);
    }
}
