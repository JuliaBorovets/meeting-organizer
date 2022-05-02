package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.ContactUsService;
import com.meeting.organizer.web.dto.v1.ContactUsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ContactUsController.BASE_URL)
public class ContactUsController {

    public static final String BASE_URL = "/api/v1/contact-us";

    private final ContactUsService contactUsService;

    @PostMapping
    public ContactUsDto createContactUsRequest(@RequestBody ContactUsDto contactUsDto) {
        return contactUsService.createContactUsRequest(contactUsDto);
    }

}
