package com.interview.service;

import com.interview.entity.User;
import com.interview.dto.UserDto;
import com.interview.entity.constance.Role;
import com.interview.entity.constance.Status;
import com.interview.exception.NotFoundException;
import com.interview.mapper.UserMapper;
import com.interview.repository.UserRepository;
import com.interview.validation.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    private static final Logger log = LogManager.getRootLogger();

    public UserService(UserRepository userRepository, UserMapper userMapper, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
    }

    public List<UserDto> findAll(){
        log.debug("find all method for users");
        return userRepository.findAll().stream()
                .filter(user -> !user.getStatus().equals(Status.DELETED))
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(long id) throws NotFoundException {
        return userRepository.findById(id).stream()
                .filter(user -> !user.getStatus().equals(Status.DELETED))
                .findFirst()
                .map(userMapper::convertToDto)
                .orElseThrow(() -> new NotFoundException("No user with id:" + id));
    }

    public User findFullInfoById(long id) throws NotFoundException {
        return userRepository.findById(id).stream()
                .filter(user -> !user.getStatus().equals(Status.DELETED))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No user with id:" + id));
    }

    public UserDto save(User user) {
        User userForSave = user.setCreationTime(OffsetDateTime.now())
                .setPassword(new BCryptPasswordEncoder().encode(user.getPassword()))
                .setRole(Role.USER)
                .setStatus(Status.ACTIVE);
        userValidator.validate(userForSave);
        return userMapper.convertToDto(userRepository.save(userForSave));
    }
}
