package com.meeting.organizer.web.dto.v1.library;

import com.meeting.organizer.utils.BaseUIResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LibraryResponse extends BaseUIResponse<LibraryDto> {
}
