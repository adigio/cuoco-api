package com.cuoco.adapter.out.email;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.application.port.out.SendConfirmationEmailRepository;
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
public class SendConfirmationNotificationEmailRepositoryAdapter implements SendConfirmationEmailRepository {

    private final String EMAIL_BODY = FileReader.execute("email/userConfirmation.html");

    @Value("${shared.email.no-reply.name}")
    private String fromName;

    @Value("${shared.email.no-reply.from}")
    private String fromEmail;

    private final HttpServletRequest request;
    private final JavaMailSender mailSender;

    @Override
    public void execute(User user, String token) {
        try {
            log.info("Executing send confirmation email for user with ID {}", user.getId());

            String confirmationLink = buildConfirmationLink(token);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress(fromEmail, fromName));
            helper.setTo(user.getEmail());
            helper.setSubject(user.getName() +", ¡Confirmá tu cuenta en Cuoco!");

            String content = EMAIL_BODY
                    .replace("{{NAME}}", user.getName())
                    .replace("{{LINK}}", confirmationLink);

            helper.setText(content, true);

            mailSender.send(message);

            log.info("Successfully sended confirmation email to {} with link {}", user.getEmail(), confirmationLink);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending confirmation email to {}: {}", user.getEmail(), e.getMessage());
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }

    private String buildConfirmationLink(String token) {

        String baseUrl = request.getRequestURL().toString()
                .replace(request.getRequestURI(), "");

        String contextPath = baseUrl.contains("cuoco.com.ar") ? "/api" : "";

        String confirmationLink = baseUrl
                .concat(contextPath)
                .concat("/auth/confirm?token=")
                .concat(token);

        log.info("Builded URL link from this base {} and this context {}", baseUrl, contextPath);
        return confirmationLink;
    }
}

