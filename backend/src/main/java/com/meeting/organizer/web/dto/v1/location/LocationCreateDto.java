package com.meeting.organizer.web.dto.v1.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationCreateDto {
    private String country;

    private String city;

    private Timestamp creationDate;
}
