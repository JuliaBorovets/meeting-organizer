package com.meeting.organizer.service;

import com.meeting.organizer.model.user.User;

public interface MailService {

    //todo
    void sendRegistrationCredentials(User user);

    //todo
    void sendRegistrationConfirmLinkMail(User user, String passwordResetLink);

    void sendResetPasswordLinkMail(User user, String passwordResetLink);

    void sendPasswordUpdateMail(User user);

}
