package com.meeting.organizer.config;


import lombok.experimental.UtilityClass;

@UtilityClass
public class MailConstants {

    public static final String REGISTRATION_MAIL_TEMPLATE = "mail/registration-mail";
    public static final String REGISTRATION_LINK_MAIL_TEMPLATE = "mail/registration-link-mail";
    public static final String RESET_PASSWORD_LINK_MAIL_TEMPLATE = "mail/reset-password-link-mail";
    public static final String PASSWORD_UPDATE_MAIL_TEMPLATE = "mail/password-update-mail";
    public static final String CONTACT_US_MAIL_TEMPLATE = "mail/contact-us";
    public static final String CONTACT_US_CONFIRM_MAIL_TEMPLATE = "mail/contact-us-confirm";
    public static final String ADD_LIBRARY_ACCESS_MAIL_TEMPLATE = "mail/add-access-library";
    public static final String REMOVE_LIBRARY_ACCESS_MAIL_TEMPLATE = "mail/remove-access-library";

    public static final String REGISTRATION_MAIL_SUBJECT = "MeetingProject registration";
    public static final String REGISTRATION_LINK_MAIL_SUBJECT = "MeetingProject verify registration";
    public static final String RESET_PASSWORD_LINK_MAIL_SUBJECT = "MeetingProject password reset";
    public static final String PASSWORD_UPDATE_MAIL_SUBJECT = "MeetingProject password update";
    public static final String CONTACT_US_MAIL_SUBJECT = "MeetingProject support";
    public static final String CONTACT_US_CONFIRM_MAIL_SUBJECT = "MeetingProject support confirmation";
    public static final String ADD_LIBRARY_ACCESS_MAIL_SUBJECT = "MeetingProject adding access to Library";
    public static final String REMOVE_LIBRARY_ACCESS_MAIL_SUBJECT = "MeetingProject removing access to Library";

}
