package com.meeting.organizer.web.dto.v1.reaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDto {

    private Long ratingId;

    private Long eventId;

    private Long userId;

    private Long username;

    private String date;

    private Long score;
}
