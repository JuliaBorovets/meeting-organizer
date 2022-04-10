package com.meeting.organizer.service;

public interface ResetPasswordService {

    void processResetPasswordRequest(String userEmail);

    void processPasswordReset(String userEmail, String newUserPassword);

    String validatePasswordResetToken(String token);

}
