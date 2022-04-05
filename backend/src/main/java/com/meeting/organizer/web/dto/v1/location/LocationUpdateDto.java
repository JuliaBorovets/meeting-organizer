package com.meeting.organizer.web.dto.v1.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationUpdateDto {
    private Long locationId;

    private String country;

    private String city;
}
