package com.meeting.organizer.web.dto.v1.library;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddLibraryAccessDto {

    List<String> emailList = new ArrayList<>();

    Long libraryId;
}
