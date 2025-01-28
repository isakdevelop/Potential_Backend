package com.potential.api.common.component;

import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.PotentialException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class SendEmailComponent {
    @Value("${SENDER_EMAIL}")
    private String senderEmail;

    @Value("${SENDER_NAME}")
    private String senderName;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendEmail(String to, String subject, String templateName, Context variables) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            String content = templateEngine.process(templateName, variables);

            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setFrom(new InternetAddress(senderEmail, senderName));
            message.setContent(content, "text/html; charset=UTF-8");

            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new PotentialException(com.potential.api.common.enums.Error.UNPROCESSABLE_ENTITY.getStatus(), Error.UNPROCESSABLE_ENTITY.getMessage());
        }
    }
}
