package com.meeting.organizer.web.dto.v1.event.webex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebexRegistrationCreateDto {

    private Boolean autoAcceptRequest;

    private Boolean requireFirstName;

    private Boolean requireLastName;

    private Boolean requireEmail;

}
