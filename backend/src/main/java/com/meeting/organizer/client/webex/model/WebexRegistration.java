package com.meeting.organizer.client.webex.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebexRegistration {

    private Boolean autoAcceptRequest;

    private Boolean requireFirstName;

    private Boolean requireLastName;

    private Boolean requireEmail;

    private Boolean requireJobTitle;

    private Boolean requireCompanyName;

    private Boolean requireAddress1;

    private Boolean requireAddress2;

    private Boolean requireCity;

    private Boolean requireState;

    private Boolean requireZipCode;

    private Boolean requireCountryRegion;

    private Boolean requireWorkPhone;

    private Boolean requireFax;

}
