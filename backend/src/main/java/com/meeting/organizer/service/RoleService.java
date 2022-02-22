package com.meeting.organizer.service;

import com.meeting.organizer.model.user.Role;

public interface RoleService {

    Role findRoleByName(String name);

    void addRoleToUser(Long userId, Long roleId);

    void deleteRoleToUser(Long userId, Long roleId);

}
