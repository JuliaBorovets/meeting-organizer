package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.DisabledUserException;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.model.user.VerificationToken;
import com.meeting.organizer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final UserService userService;
    private final CRUDService<User> userCRUDService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final VerificationTokenService verificationTokenService;

    @Value("${reset.password.confirm.link}")
    private String resetPasswordLink;

    public ResetPasswordServiceImpl(UserService userService,
                                    @Qualifier("userServiceImpl") CRUDService<User> userCRUDService,
                                    PasswordEncoder passwordEncoder,
                                    MailService mailService,
                                    VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.userCRUDService = userCRUDService;
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

    @Override
    public void processPasswordReset(String userEmail, String newUserPassword) {
        User user = userService.findByEmail(userEmail);

        if (!user.isAccountNonLocked()) {
            throw new DisabledUserException();
        }

        user.setPassword(passwordEncoder.encode(newUserPassword));
        userCRUDService.save(user);
        mailService.sendPasswordUpdateMail(user);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        VerificationToken verificationToken = verificationTokenService.validateVerificationToken(token);
        return userCRUDService.findById(verificationToken.getUser().getUserId()).getEmail();
    }
}
