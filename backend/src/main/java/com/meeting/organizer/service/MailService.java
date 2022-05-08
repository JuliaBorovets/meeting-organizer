package com.meeting.organizer.service;

import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.web.dto.v1.ContactUsDto;

public interface MailService {

    void sendRegistrationCredentials(User user);

    void sendRegistrationConfirmLinkMail(User user, String passwordResetLink);

    void sendResetPasswordLinkMail(User user, String passwordResetLink);

    void sendPasswordUpdateMail(User user);

    void sendContactUsRequest(ContactUsDto contactUsDto, String email);

    void sendContactUsConfirmation(String link);

    void sendAddLibraryAccessMail(User user, Library library);

    void sendRemoveLibraryAccessMail(User user, Library library);
}
