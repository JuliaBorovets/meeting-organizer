package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(VerificationTokenController.BASE_URL)
@RequiredArgsConstructor
public class VerificationTokenController {

    public static final String BASE_URL = "/api/v1/user/verify";

    private final UserService userService;

    @GetMapping("/registration-confirm")
    public void confirmRegistration(@RequestParam("token") String token) {
        userService.confirmRegistration(token);
    }

    @GetMapping("/resend-registration-token")
    public void resendRegistrationToken(@RequestParam("token") String existingToken, HttpServletRequest request) {
        userService.resendRegistrationConfirmLink(existingToken);
    }
}
