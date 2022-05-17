package com.meeting.organizer.service;

import com.meeting.organizer.model.Comment;
import com.meeting.organizer.web.dto.v1.reaction.CommentCreateDto;
import com.meeting.organizer.web.dto.v1.reaction.CommentDto;
import com.meeting.organizer.web.dto.v1.reaction.CommentResponse;
import com.meeting.organizer.web.dto.v1.reaction.CommentUpdateDto;
import org.springframework.data.domain.Pageable;


public interface CommentService {

    CommentDto createComment(CommentCreateDto commentCreateDto);

    CommentDto updateComment(CommentUpdateDto commentUpdateDto);

    Comment findCommentById(Long id);

    void deleteComment(Long id);

    CommentResponse findByEventId(Long eventId, Pageable pageable);
}
