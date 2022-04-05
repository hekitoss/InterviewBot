package com.interview.service;

import com.interview.dto.AuthenticateRequestDto;
import com.interview.entity.User;
import com.interview.exception.CustomJwtException;
import com.interview.repository.UserRepository;
import com.interview.security.JwtTokenProvider;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }


    public Map<Object, Object> authenticate(AuthenticateRequestDto request) {
        log.debug("authenticate for users + " + request.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = userRepository.findUserByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtTokenProvider.createToken(request.getUsername(), user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("username", request.getUsername());
            response.put("token", token);
            return response;
    }

    public User getCurrentUser() {
        try {
            return userRepository.findUserByUsername(
                    ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername())
                    .orElseThrow(() -> new CustomJwtException("Not unauthorized ", HttpStatus.UNAUTHORIZED));
        } catch (ClassCastException ex) {
            throw new CustomJwtException("Not unauthorized ", HttpStatus.UNAUTHORIZED);
        }
    }
}
