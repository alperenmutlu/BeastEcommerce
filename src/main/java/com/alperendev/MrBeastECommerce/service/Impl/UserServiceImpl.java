package com.alperendev.MrBeastECommerce.service.Impl;

import com.alperendev.MrBeastECommerce.dto.LoginRequest;
import com.alperendev.MrBeastECommerce.dto.Response;
import com.alperendev.MrBeastECommerce.dto.UserDto;
import com.alperendev.MrBeastECommerce.entity.User;
import com.alperendev.MrBeastECommerce.enums.UserRole;
import com.alperendev.MrBeastECommerce.exception.InvalidCredentialsException;
import com.alperendev.MrBeastECommerce.exception.NotFoundException;
import com.alperendev.MrBeastECommerce.mapper.EntityDtoMapper;
import com.alperendev.MrBeastECommerce.repository.UserRepository;
import com.alperendev.MrBeastECommerce.security.JwtUtils;
import com.alperendev.MrBeastECommerce.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response registerUser(UserDto registrationRequest) {
        UserRole role = UserRole.USER;

        if(registrationRequest.getRole() != null && registrationRequest.getRole().equalsIgnoreCase("admin")) {
            role = UserRole.ADMIN;
        }

        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        UserDto userDto = entityDtoMapper.mapUserToDtoBasic(savedUser);
        return Response.builder()
                .status(200)
                .message("User successfully added")
                .user(userDto)
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(
                loginRequest.getEmail()).orElseThrow(() ->
                new NotFoundException("Email not found"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Password incorrect");
        }
        String token = jwtUtils.generateToken(user);
        return Response.builder()
                .status(200)
                .message("User successfully logged in!")
                .token(token)
                .expirationTime("6 Month")
                .role(user.getRole().name())
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(entityDtoMapper::mapUserToDtoBasic)
                .toList();
        return Response.builder()
                .status(200)
                .userList(userDtos)
                .build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); //suppose email is the username
        log.info("User logged in with the email: {}", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);
        return Response.builder()
                .status(200)
                .user(userDto)
                .build();
    }
}
