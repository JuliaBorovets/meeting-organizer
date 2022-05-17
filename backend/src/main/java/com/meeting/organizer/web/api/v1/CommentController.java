package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.CommentService;
import com.meeting.organizer.web.dto.v1.reaction.CommentCreateDto;
import com.meeting.organizer.web.dto.v1.reaction.CommentDto;
import com.meeting.organizer.web.dto.v1.reaction.CommentResponse;
import com.meeting.organizer.web.dto.v1.reaction.CommentUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(CommentController.BASE_URL)
public class CommentController {

    public static final String BASE_URL = "/api/v1/comment";
    private final CommentService commentService;

    @GetMapping
    public CommentResponse findAllByEventId(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                            @RequestParam Long eventId) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return commentService.findByEventId(eventId, pageable);
    }

    @PostMapping
    public CommentDto create(@RequestBody CommentCreateDto createDto) {
        return commentService.createComment(createDto);
    }

    @PutMapping
    public CommentDto update(@RequestBody CommentUpdateDto updateDto) {
        return commentService.updateComment(updateDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
