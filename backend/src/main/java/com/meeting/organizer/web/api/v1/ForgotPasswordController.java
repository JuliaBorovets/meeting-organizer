package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.ResetPasswordService;
import com.meeting.organizer.web.dto.v1.user.ForgotPasswordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ForgotPasswordController.BASE_URL)
@RequiredArgsConstructor
public class ForgotPasswordController {
    public static final String BASE_URL = "/api/v1/user/forgot-password";

    private final ResetPasswordService resetPasswordService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resetPasswordRequest(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        resetPasswordService.processResetPasswordRequest(forgotPasswordDto.getEmail());
    }

    @GetMapping("/confirm-reset")
    public String validateResetToken(@RequestParam("token") String tokenValue) {
        return resetPasswordService.validatePasswordResetToken(tokenValue);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        resetPasswordService.processPasswordReset(forgotPasswordDto.getEmail(), forgotPasswordDto.getPassword());
    }
}