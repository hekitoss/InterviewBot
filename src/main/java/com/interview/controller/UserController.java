package com.interview.controller;

import com.interview.dto.UserDto;
import com.interview.entity.User;
import com.interview.exception.NotFoundException;
import com.interview.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('USER_READ')")
    public ResponseEntity<List<UserDto>> getAll(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER_ADD')")
    public ResponseEntity<UserDto> create(@RequestBody User user){
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER_READ')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/fullInfo/{id}")
    @PreAuthorize("hasAnyAuthority('USERS_GET_FULL_INFO')")
    public ResponseEntity<User> getUserByIdFullInfo(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(userService.findFullInfoById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USERS_DELETE')")
    public ResponseEntity<UserDto> delete(@PathVariable long id) throws NotFoundException {
        return new ResponseEntity<>(userService.deleteById(id), HttpStatus.OK);
    }

    @PutMapping("/ban/{id}")
    @PreAuthorize("hasAnyAuthority('USERS_BAN')")
    public ResponseEntity<UserDto> ban(@PathVariable long id) throws NotFoundException {
        return new ResponseEntity<>(userService.banById(id), HttpStatus.OK);
    }
}
