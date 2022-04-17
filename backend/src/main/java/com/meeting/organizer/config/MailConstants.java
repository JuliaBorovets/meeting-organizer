package com.meeting.organizer.config;


import lombok.experimental.UtilityClass;

@UtilityClass
public class MailConstants {

    public static final String REGISTRATION_MAIL_TEMPLATE = "mail/registration-mail";
    public static final String REGISTRATION_LINK_MAIL_TEMPLATE = "mail/registration-link-mail";
    public static final String RESET_PASSWORD_LINK_MAIL_TEMPLATE = "mail/reset-password-link-mail";
    public static final String PASSWORD_UPDATE_MAIL_TEMPLATE = "mail/password-update-mail";

    public static final String REGISTRATION_MAIL_SUBJECT = "MeetingProject registration";
    public static final String REGISTRATION_LINK_MAIL_SUBJECT = "MeetingProject verify registration";
    public static final String RESET_PASSWORD_LINK_MAIL_SUBJECT = "MeetingProject password reset";
    public static final String PASSWORD_UPDATE_MAIL_SUBJECT = "MeetingProject password update";
}