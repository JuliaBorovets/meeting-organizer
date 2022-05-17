package com.meeting.organizer.web.dto.v1.reaction;

import com.meeting.organizer.utils.BaseUIResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CommentResponse extends BaseUIResponse<CommentDto> {
}
