package com.interview.controller.v2;

import com.interview.dto.AuthenticateRequestDto;
import com.interview.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v2/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String login(Model model, String error) {
        if (error != null) {
            model.addAttribute("error", "Username or password incorrect");
        }
        return "login";
    }

    @PostMapping("/login")
    public String authenticate(String password, String username){
        AuthenticateRequestDto authenticateRequestDto = new AuthenticateRequestDto();
        authenticateRequestDto.setUsername(username);
        authenticateRequestDto.setPassword(password);
        authenticationService.authenticate(authenticateRequestDto);
        return "login";
    }
}
