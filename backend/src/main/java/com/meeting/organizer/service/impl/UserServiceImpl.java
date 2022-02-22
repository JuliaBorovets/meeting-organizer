package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.RegistrationException;
import com.meeting.organizer.model.user.Role;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.repository.user.UserRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.RoleService;
import com.meeting.organizer.service.UserService;
import com.meeting.organizer.web.dto.v1.user.UserCreateDto;
import com.meeting.organizer.web.dto.v1.user.UserDto;
import com.meeting.organizer.web.mapper.v1.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends AbstractService<User, UserRepository> implements UserService {

    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository,
                           UserMapper userMapper,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        super(repository);
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserDto saveNewUserDto(UserCreateDto userDto) {
        User user = userMapper.createDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role defaultRole = roleService.findRoleByName("USER");
        user.getRoles().add(defaultRole);

        User savedUser;
        try {
            savedUser = super.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RegistrationException("username or email exists");
        }

        return userMapper.userToUserDto(savedUser);
    }
}
