package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.RoleCanNotFindException;
import com.meeting.organizer.exception.custom.UserNotFoundException;
import com.meeting.organizer.model.user.Role;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.repository.user.RoleRepository;
import com.meeting.organizer.repository.user.UserRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class RoleServiceImpl extends AbstractService<Role, RoleRepository> implements RoleService {

    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository repository,
                           UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Override
    public Role findRoleByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RoleCanNotFindException("can not find role with name=" + name));
    }

    @Transactional
    @Override
    public void addRoleToUser(Long userId, Long roleId) {
        log.debug("RoleServiceImpl addRoleToUser, userId={}, roleId={}", userId, roleId);
        Role role = findById(roleId);
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.getRoles().add(role);
        role.getUsers().add(user);
        userRepository.save(user);
        super.save(role);

        log.debug("roleId={} was added to userId={}", roleId, userId);
    }

    @Transactional
    @Override
    public void deleteRoleToUser(Long userId, Long roleId) {
        log.debug("RoleServiceImpl deleteRoleToUser, userId={}, roleId={}", userId, roleId);
        Role role = findById(roleId);
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.getRoles().remove(role);
        role.getUsers().remove(user);
        userRepository.save(user);
        super.save(role);

        log.debug("roleId={} was deleted from userId={}", roleId, userId);
    }

    private Role findById(Long id) {
        return repository.findById(id)
                .orElseThrow(RoleCanNotFindException::new);
    }

}
