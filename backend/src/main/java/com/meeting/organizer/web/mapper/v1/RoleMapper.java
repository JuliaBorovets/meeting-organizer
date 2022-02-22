package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.user.Role;
import com.meeting.organizer.web.dto.v1.user.RoleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role dtoToRole(RoleDto roleDto);

    RoleDto roleToRoleDto(Role role);
}
