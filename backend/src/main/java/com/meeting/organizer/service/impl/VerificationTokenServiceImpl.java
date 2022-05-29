package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.VerificationTokenException;
import com.meeting.organizer.exception.custom.VerificationTokenNotFoundException;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.model.user.VerificationToken;
import com.meeting.organizer.repository.user.VerificationTokenRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.CRUDService;
import com.meeting.organizer.service.UserService;
import com.meeting.organizer.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class VerificationTokenServiceImpl extends AbstractService<VerificationToken, VerificationTokenRepository> implements VerificationTokenService {

    private final UserService userService;

    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository,
                                        UserService userService) {
        super(verificationTokenRepository);
        this.userService = userService;
    }

    @Override
    public VerificationToken createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);

        return repository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return repository.findByToken(verificationToken)
                .orElseThrow(() -> new VerificationTokenNotFoundException("can not find token " + verificationToken));
    }

    @Override
    public VerificationToken validateVerificationToken(String token) {
        VerificationToken verificationToken = getVerificationToken(token);

        User user = verificationToken.getUser();

        if (verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            user.setEnabled(true);
            userService.save(user);
        } else {
            throw new VerificationTokenException();
        }

        repository.delete(verificationToken);
        return verificationToken;
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingVerificationToken) {
        VerificationToken newToken = getVerificationToken(existingVerificationToken);
        newToken.setToken(UUID.randomUUID().toString());

        return repository.save(newToken);
    }
}
