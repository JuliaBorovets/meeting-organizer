package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.Comment;
import com.meeting.organizer.web.dto.v1.reaction.CommentCreateDto;
import com.meeting.organizer.web.dto.v1.reaction.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment commentDtoToComment(CommentCreateDto commentDto);

    @Mappings({
            @Mapping(target = "userId", source = "comment.user.userId"),
            @Mapping(target = "username", source = "comment.user.username"),
            @Mapping(target = "eventId", source = "comment.event.eventId"),
    })
    CommentDto commentToCommentDto(Comment comment);
}
