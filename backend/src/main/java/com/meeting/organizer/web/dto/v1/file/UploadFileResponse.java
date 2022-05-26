package com.meeting.organizer.web.dto.v1.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFileResponse {

    private String fileName;

    private String fileDownloadUri;

    private String fileType;

    private long size;

}
