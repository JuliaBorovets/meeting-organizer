package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.RegistrationException;
import com.meeting.organizer.exception.custom.UserNotFoundException;
import com.meeting.organizer.model.user.Role;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.model.user.VerificationToken;
import com.meeting.organizer.repository.user.UserRepository;
import com.meeting.organizer.service.*;
import com.meeting.organizer.web.dto.v1.user.UserCreateDto;
import com.meeting.organizer.web.dto.v1.user.UserDto;
import com.meeting.organizer.web.dto.v1.user.UserResponse;
import com.meeting.organizer.web.dto.v1.user.UserUpdateDto;
import com.meeting.organizer.web.mapper.v1.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends AbstractService<User, UserRepository> implements UserService {

    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final VerificationTokenService verificationTokenService;
    private final FileStorageService fileStorageService;

    @Value("${registration.confirm.link}")
    private String registrationConfirmLink;

    public UserServiceImpl(UserRepository repository,
                           UserMapper userMapper,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder,
                           MailService mailService,
                           FileStorageService fileStorageService,
                           @Lazy VerificationTokenService verificationTokenService) {
        super(repository);
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.verificationTokenService = verificationTokenService;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
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

        VerificationToken token = verificationTokenService.createVerificationToken(savedUser, UUID.randomUUID().toString());
        mailService.sendRegistrationConfirmLinkMail(savedUser, String.format(registrationConfirmLink, token.getToken()));

        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email=" + email));
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id=" + id));
    }

    @Override
    public void confirmRegistration(String token) {

        VerificationToken verificationToken = verificationTokenService.validateVerificationToken(token);
        mailService.sendRegistrationCredentials(verificationToken.getUser());
    }

    @Override
    public void resendRegistrationConfirmLink(String token) {

        VerificationToken verificationToken = verificationTokenService.generateNewVerificationToken(token);
        mailService.sendResetPasswordLinkMail(verificationToken.getUser(), String.format(registrationConfirmLink, token));
    }

    @Override
    public UserDto getById(Long id) {
        return userMapper.userToUserDto(
                findById(id)
        );
    }

    @Override
    public UserDto update(UserUpdateDto userUpdateDto) {
        User user = findById(userUpdateDto.getUserId());
        user.setEmail(userUpdateDto.getEmail());
        user.setUsername(userUpdateDto.getUsername());
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));

        return userMapper.userToUserDto(
                repository.save(user)
        );
    }

    @Override
    public UserResponse getEventVisitors(Long eventId, String username, Pageable pageable) {
        UserResponse response = new UserResponse();
        String usernamePattern = username + "%";

        List<UserDto> userDtoList = repository.findAllByVisitedEvents_EventIdAndUsernameLike(eventId, usernamePattern, pageable)
                .stream().map(userMapper::userToUserDto)
                .collect(Collectors.toList());

        Long total = repository.countAllByVisitedEvents_EventId(eventId);

        response.setList(userDtoList);
        response.setTotalItems(total);
        return response;
    }

    @Transactional
    @Override
    public UserDto uploadUserImage(Long userId, MultipartFile image) {
        User user = findById(userId);
        if (Objects.nonNull(user.getImagePath())) {
            fileStorageService.deleteFile(user.getImagePath());
        }
        user.setImagePath(fileStorageService.storeFile(image));
        return userMapper.userToUserDto(user);
    }

}
