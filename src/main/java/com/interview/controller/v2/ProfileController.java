package com.interview.controller.v2;

import com.interview.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v2")
public class ProfileController {
    private final AuthenticationService authenticationService;

    public ProfileController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/profile")
    public String greeting(Model model) {
        model.addAttribute("name", authenticationService.getCurrentUser().getUsername());
        return "profile";
    }

}
