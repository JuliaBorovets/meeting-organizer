package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.CommentNotFoundException;
import com.meeting.organizer.model.Comment;
import com.meeting.organizer.model.Event;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.repository.CommentRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.CRUDService;
import com.meeting.organizer.service.CommentService;
import com.meeting.organizer.web.dto.v1.reaction.CommentCreateDto;
import com.meeting.organizer.web.dto.v1.reaction.CommentDto;
import com.meeting.organizer.web.dto.v1.reaction.CommentResponse;
import com.meeting.organizer.web.dto.v1.reaction.CommentUpdateDto;
import com.meeting.organizer.web.mapper.v1.CommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl extends AbstractService<Comment, CommentRepository> implements CommentService {

    private final CommentMapper commentMapper;
    private final CRUDService<User> userCRUDService;
    private final CRUDService<Event> eventCRUDService;


    public CommentServiceImpl(CommentRepository repository,
                              CommentMapper commentMapper,
                              @Qualifier("userServiceImpl") CRUDService<User> userCRUDService,
                              @Qualifier("eventServiceImpl") CRUDService<Event> eventCRUDService) {
        super(repository);
        this.commentMapper = commentMapper;
        this.userCRUDService = userCRUDService;
        this.eventCRUDService = eventCRUDService;
    }


    @Override
    public CommentDto createComment(CommentCreateDto commentCreateDto) {
        Comment comment = commentMapper.commentDtoToComment(commentCreateDto);
        User user = userCRUDService.findById(commentCreateDto.getUserId());
        Event event = eventCRUDService.findById(commentCreateDto.getEventId());

        comment.setUser(user);
        comment.setEvent(event);

        event.getComments().add(comment);
        user.getComments().add(comment);

        Comment savedComment = repository.save(comment);

        userCRUDService.save(user);
        eventCRUDService.save(event);

        return convertToDto(savedComment);
    }

    @Transactional
    @Override
    public CommentDto updateComment(CommentUpdateDto commentUpdateDto) {
        Comment comment = findCommentById(commentUpdateDto.getCommentId());
        comment.setText(commentUpdateDto.getText());

        return convertToDto(
                repository.save(comment)
        );
    }

    @Override
    public Comment findCommentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id=" + id));
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = findCommentById(id);

        repository.deleteById(comment.getCommentId());
    }

    @Override
    public CommentResponse findByEventId(Long eventId, Pageable pageable) {
        CommentResponse response = new CommentResponse();

        List<CommentDto> commentDtos = repository.findAllByEvent_EventIdOrderByCreationDateDesc(eventId, pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        response.setList(commentDtos);
        response.setTotalItems(repository.countAllByEvent_EventIdOrderByCreationDateDesc(eventId));

        return response;
    }

    private CommentDto convertToDto(Comment comment) {
        return commentMapper.commentToCommentDto(comment);
    }
}
