package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.DisabledUserException;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.model.user.VerificationToken;
import com.meeting.organizer.service.MailService;
import com.meeting.organizer.service.ResetPasswordService;
import com.meeting.organizer.service.UserService;
import com.meeting.organizer.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final VerificationTokenService verificationTokenService;

    @Value("${reset.password.confirm.link}")
    private String resetPasswordLink;

    public ResetPasswordServiceImpl(UserService userService,
                                    PasswordEncoder passwordEncoder,
                                    MailService mailService,
                                    VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.verificationTokenService = verificationTokenService;
    }


    @Override
    public void processResetPasswordRequest(String userEmail) {
        User user = userService.findByEmail(userEmail);

        if (!user.isAccountNonLocked()) {
            throw new DisabledUserException();
        }

        VerificationToken token = verificationTokenService.createVerificationToken(user, UUID.randomUUID().toString());

        mailService.sendResetPasswordLinkMail(user, String.format(resetPasswordLink, token.getToken()));
    }

    @Transactional
    @Override
    public void processPasswordReset(String userEmail, String newUserPassword) {
        User user = userService.findByEmail(userEmail);

        if (!user.isAccountNonLocked()) {
            throw new DisabledUserException();
        }

        user.setPassword(passwordEncoder.encode(newUserPassword));
        mailService.sendPasswordUpdateMail(user);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        VerificationToken verificationToken = verificationTokenService.validateVerificationToken(token);
        return userService.findById(verificationToken.getUser().getUserId()).getEmail();
    }
}
