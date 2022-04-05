package com.interview.controller.v2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v2")
public class CheckController {

    @GetMapping("/check")
    public String greeting(Model model) {
        model.addAttribute("name", "Hekitos");
        return "check";
    }

}
