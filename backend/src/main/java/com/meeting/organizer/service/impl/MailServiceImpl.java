package com.meeting.organizer.service.impl;

import com.meeting.organizer.config.MailConstants;
import com.meeting.organizer.exception.custom.MailSendException;
import com.meeting.organizer.model.Event;
import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.service.MailService;
import com.meeting.organizer.web.dto.v1.ContactUsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
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
        sendMessage(user.getEmail(),
                MailConstants.REGISTRATION_MAIL_SUBJECT,
                MailConstants.REGISTRATION_MAIL_TEMPLATE,
                Map.of("user", user)
        );
    }

    @Override
    public void sendRegistrationConfirmLinkMail(User user, String passwordResetLink) {
        sendMessage(user.getEmail(),
                MailConstants.REGISTRATION_LINK_MAIL_SUBJECT,
                MailConstants.REGISTRATION_LINK_MAIL_TEMPLATE,
                Map.of("link", passwordResetLink)
        );
    }

    @Override
    public void sendResetPasswordLinkMail(User user, String passwordResetLink) {
        sendMessage(user.getEmail(),
                MailConstants.RESET_PASSWORD_LINK_MAIL_SUBJECT,
                MailConstants.RESET_PASSWORD_LINK_MAIL_TEMPLATE,
                Map.of("user", user,
                        "link", passwordResetLink));
    }

    @Override
    public void sendPasswordUpdateMail(User user) {
        sendMessage(user.getEmail(),
                MailConstants.PASSWORD_UPDATE_MAIL_SUBJECT,
                MailConstants.PASSWORD_UPDATE_MAIL_TEMPLATE,
                Map.of("user", user));
    }

    @Override
    public void sendContactUsRequest(ContactUsDto contactUsDto, String email) {
        sendMessage(email,
                MailConstants.CONTACT_US_MAIL_SUBJECT,
                MailConstants.CONTACT_US_MAIL_TEMPLATE,
                Map.of("contactUsDto", contactUsDto));
    }

    @Override
    public void sendContactUsConfirmation(String email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Context context = new Context();
        context.setVariable("email", email);
        String text = templateEngine.process(MailConstants.CONTACT_US_CONFIRM_MAIL_TEMPLATE, context);

        try {
            helper.setTo(email);
            helper.setSubject(MailConstants.CONTACT_US_CONFIRM_MAIL_SUBJECT);
            helper.setText(text, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Failed to send mail!", e);
        }
    }

    @Override
    public void sendAddLibraryAccessMail(User user, Library library) {
        sendMessage(user.getEmail(),
                MailConstants.ADD_LIBRARY_ACCESS_MAIL_SUBJECT,
                MailConstants.ADD_LIBRARY_ACCESS_MAIL_TEMPLATE,
                Map.of("library", library,
                        "user", user));
    }

    @Override
    public void sendRemoveLibraryAccessMail(User user, Library library) {
        sendMessage(user.getEmail(),
                MailConstants.REMOVE_LIBRARY_ACCESS_MAIL_SUBJECT,
                MailConstants.REMOVE_LIBRARY_ACCESS_MAIL_TEMPLATE,
                Map.of("library", library,
                        "user", user));
    }

    @Override
    public void sendAddEventAccessMail(User user, Event event) {
        sendMessage(user.getEmail(),
                MailConstants.ADD_EVENT_ACCESS_MAIL_SUBJECT,
                MailConstants.ADD_EVENT_ACCESS_MAIL_TEMPLATE,
                Map.of("event", event,
                        "user", user));
    }

    @Override
    public void sendRemoveEventAccessMail(User user, Event event) {
        sendMessage(user.getEmail(),
                MailConstants.REMOVE_EVENT_ACCESS_MAIL_SUBJECT,
                MailConstants.REMOVE_EVENT_ACCESS_MAIL_TEMPLATE,
                Map.of("event", event,
                        "user", user));
    }

    @Override
    public void sendEventNotificationMail(User user, Event event, long minutes) {
        sendMessage(user.getEmail(),
                MailConstants.EVENT_NOTIFICATION_MAIL_SUBJECT,
                MailConstants.EVENT_NOTIFICATION_MAIL_TEMPLATE,
                Map.of("event", event,
                        "user", user,
                        "minutes", minutes
                        ));
    }

    private void sendMessage(String email, String subject, String template, Map<String, Object> params) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Context context = new Context();
        params.forEach(context::setVariable);
        String text = templateEngine.process(template, context);

        try {
            helper.setTo(email);
            helper.setFrom("juliaborovets2001@gmail.com");
            helper.setSubject(subject);
            helper.setText(text, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Failed to send mail!", e);
        }
    }
}
