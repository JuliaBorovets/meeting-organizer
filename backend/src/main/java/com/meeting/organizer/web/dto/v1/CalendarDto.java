package com.meeting.organizer.web.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarDto {

    private Long id;

    private String title;

    private String start;

    private String end;

    private String url;

}
