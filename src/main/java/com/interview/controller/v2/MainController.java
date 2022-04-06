package com.interview.controller.v2;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v2")
public class MainController {

    @GetMapping("/about")
    @PreAuthorize("permitAll()")
    public String about() {
        return "about";
    }
}
