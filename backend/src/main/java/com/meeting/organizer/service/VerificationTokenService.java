package com.meeting.organizer.service;

import com.meeting.organizer.model.user.User;
import com.meeting.organizer.model.user.VerificationToken;

public interface VerificationTokenService {

    VerificationToken createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);

    VerificationToken validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String existingVerificationToken);

}
