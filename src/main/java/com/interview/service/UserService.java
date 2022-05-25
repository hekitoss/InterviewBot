package com.interview.service;

import com.interview.entity.User;
import com.interview.dto.UserDto;
import com.interview.entity.constance.Role;
import com.interview.entity.constance.Status;
import com.interview.exception.NotFoundException;
import com.interview.logger.Audit;
import com.interview.mapper.UserMapper;
import com.interview.repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Log4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator userValidator;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Audit
    public List<UserDto> findAll(){
        log.debug("find all method for users");
        return userRepository.findAll().stream()
                .filter(user -> !user.getStatus().equals(Status.DELETED))
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Audit
    public UserDto findById(Long id) throws NotFoundException {
        log.debug("find user by id: " + id);
        return userRepository.findById(id).stream()
                .filter(user -> !user.getStatus().equals(Status.DELETED))
                .findFirst()
                .map(userMapper::convertToDto)
                .orElseThrow(() -> new NotFoundException("Not found user with id:" + id));
    }

    @Audit
    public User findFullInfoById(Long id) throws NotFoundException {
        log.debug("find full info for user with id: " + id);
        return userRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found user with id:" + id));
    }

    @Audit
    public UserDto save(User user) {
        log.debug("save user method for user: " + user);
        userValidator.validate(user);
        return userMapper.convertToDto(userRepository.save(user));
    }

    @Audit
    public UserDto deleteById(Long id) throws NotFoundException {
        log.debug("delete user by id method, with id: " + id);
        return userRepository.findById(id).stream()
                .peek(user -> save(user.setStatus(Status.DELETED).setDeletingTime(OffsetDateTime.now())))
                .map(userMapper::convertToDto)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + id));
    }

    @Audit
    public UserDto changeStatusById(Long id, String status) throws NotFoundException {
        log.debug("change status method for user with id: " + id + " on " + status);
        return userRepository.findById(id).stream()
                .peek(user -> save(user.setStatus(Status.valueOf(status.toUpperCase(Locale.ROOT)))))
                .map(userMapper::convertToDto)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found user with id: " + id));
    }

    public UserDto create(User user) {
        return save(user.setCreationTime(OffsetDateTime.now())
                .setPassword(new BCryptPasswordEncoder().encode(user.getPassword()))
                .setRole(Role.USER)
                .setStatus(user.getStatus() == null ? Status.ACTIVE: user.getStatus()));
    }
}
