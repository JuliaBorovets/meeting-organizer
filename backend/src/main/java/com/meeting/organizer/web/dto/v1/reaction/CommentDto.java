package com.meeting.organizer.web.dto.v1.reaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long commentId;

    private Long eventId;

    private Long userId;

    private String username;

    private String text;

    private String creationDate;

}
