package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.Rating;
import com.meeting.organizer.web.dto.v1.reaction.RatingCreateDto;
import com.meeting.organizer.web.dto.v1.reaction.RatingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    Rating ratingDtoToRating(RatingCreateDto createDto);

    @Mappings({
            @Mapping(target = "userId", source = "rating.user.userId"),
            @Mapping(target = "username", source = "rating.user.username"),
            @Mapping(target = "eventId", source = "rating.event.eventId"),
    })
    RatingDto ratingToRatingDto(Rating rating);
}
