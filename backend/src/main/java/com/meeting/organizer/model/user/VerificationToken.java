package com.meeting.organizer.model.user;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "verification_token")
@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
@Slf4j
@AllArgsConstructor
public class VerificationToken {

    private static final int EXPIRATION = 60 * 24;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.expiryDate = LocalDateTime.now().plus(EXPIRATION, ChronoUnit.MINUTES);
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationTokenId;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

}
