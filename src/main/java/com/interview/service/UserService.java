package com.interview.service;

import com.interview.entity.User;
import com.interview.dto.UserDto;
import com.interview.entity.constance.Status;
import com.interview.exception.NotFoundException;
import com.interview.mapper.UserMapper;
import com.interview.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final Logger log = LogManager.getRootLogger();

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
}
