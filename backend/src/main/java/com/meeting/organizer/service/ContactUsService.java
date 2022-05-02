package com.meeting.organizer.service;

import com.meeting.organizer.web.dto.v1.ContactUsDto;

public interface ContactUsService {

    ContactUsDto createContactUsRequest(ContactUsDto contactUsDto);
}
