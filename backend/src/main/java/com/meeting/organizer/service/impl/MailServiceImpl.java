package com.meeting.organizer.service.impl;

import com.meeting.organizer.config.MailConstants;
import com.meeting.organizer.exception.custom.MailSendException;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendRegistrationCredentials(User user) {
        sendMessage(user,
                MailConstants.REGISTRATION_MAIL_SUBJECT,
                MailConstants.REGISTRATION_MAIL_TEMPLATE,
                Map.of("user", user)
        );
    }

    @Override
    public void sendRegistrationConfirmLinkMail(User user, String passwordResetLink) {
        sendMessage(user,
                MailConstants.REGISTRATION_LINK_MAIL_SUBJECT,
                MailConstants.REGISTRATION_LINK_MAIL_TEMPLATE,
                Map.of("link", passwordResetLink)
        );
    }

    @Override
    public void sendResetPasswordLinkMail(User user, String passwordResetLink) {
        sendMessage(user,
                MailConstants.RESET_PASSWORD_LINK_MAIL_SUBJECT,
                MailConstants.RESET_PASSWORD_LINK_MAIL_TEMPLATE,
                Map.of("user", user,
                        "link", passwordResetLink));
    }

    @Override
    public void sendPasswordUpdateMail(User user) {
        sendMessage(user,
                MailConstants.PASSWORD_UPDATE_MAIL_SUBJECT,
                MailConstants.PASSWORD_UPDATE_MAIL_TEMPLATE,
                Map.of("user", user));
    }

    private void sendMessage(User user, String subject, String template, Map<String, Object> params) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Context context = new Context();
        params.forEach(context::setVariable);
        String text = templateEngine.process(template, context);

        try {
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(text, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Failed to send mail!", e);
        }
    }
}
