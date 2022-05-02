package com.meeting.organizer.service.impl;

import com.meeting.organizer.service.ContactUsService;
import com.meeting.organizer.service.MailService;
import com.meeting.organizer.web.dto.v1.ContactUsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ContactUsServiceImpl implements ContactUsService {

    private final MailService mailService;

    @Value("${contact-us.email}")
    private String contactUsEmail;

    @Override
    public ContactUsDto createContactUsRequest(ContactUsDto contactUsDto) {

        mailService.sendContactUsRequest(contactUsDto, contactUsEmail);
        mailService.sendContactUsConfirmation(contactUsDto.getEmail());

        return contactUsDto;
    }
}
