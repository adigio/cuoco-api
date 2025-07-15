package com.cuoco.adapter.out.email;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.application.port.out.SendConfirmationEmailRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendConfirmationNotificationEmailRepositoryAdapter implements SendConfirmationEmailRepository {

    private final HttpServletRequest request;
    private final JavaMailSender mailSender;

    @Override
    public void execute(User user, String token) {
        try {
            log.info("Executing send confirmation email for user with ID {}", user.getId());

            String confirmationLink = buildConfirmationLink(token);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("noreply@cuoco.com.ar");
            helper.setTo(user.getEmail());
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

            log.info("Successfully sended confirmation email to {} with link {}", user.getEmail(), confirmationLink);
        } catch (MessagingException e) {
            log.error("Error sending confirmation email to {}: {}", user.getEmail(), e.getMessage());
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }

    private String buildConfirmationLink(String token) {

        String baseUrl = request.getRequestURL().toString()
                .replace(request.getRequestURI(), "");

        String contextPath = request.getContextPath();

        String confirmationLink = baseUrl
                .concat(contextPath)
                .concat("/auth/confirm?token=")
                .concat(token);

        log.info("Builded URL link from this base {} and this context {}", baseUrl, contextPath);
        return confirmationLink;
    }
}

