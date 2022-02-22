package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.UserNotFoundException;
import com.meeting.organizer.model.user.Role;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.repository.user.UserRepository;
import com.meeting.organizer.security.JwtUtils;
import com.meeting.organizer.service.AuthorizationService;
import com.meeting.organizer.web.dto.v1.user.UserDto;
import com.meeting.organizer.web.dto.v1.user.UserLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public UserLoginDto authenticate(UserDto userDto) {
        String bearer = "Bearer ";

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new UserLoginDto(user.getUserId(), user.getUsername(), user.getEmail(), bearer + jwt, roles);
    }

}
