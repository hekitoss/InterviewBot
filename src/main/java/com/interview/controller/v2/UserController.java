package com.interview.controller.v2;

import com.interview.dto.UserDto;
import com.interview.entity.User;
import com.interview.exception.NotFoundException;
import com.interview.service.AuthenticationService;
import com.interview.service.QuestionService;
import com.interview.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v2/users")
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final QuestionService questionService;

    public UserController(AuthenticationService authenticationService, UserService userService, QuestionService questionService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model) {
        User currentUser = authenticationService.getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("questions_number", questionService.countQuestionByCreatorId(currentUser.getId()));
        return "profile";
    }

    @GetMapping("/add")
    @PreAuthorize("permitAll()")
    public String userAdd() {
        return "user add";
    }

    @PostMapping("/add")
    @PreAuthorize("permitAll()")
    public String userAdd(@RequestParam String name, @RequestParam String surname, @RequestParam String username, @RequestParam String password) {
        userService.save(new User().setName(name).setSurname(surname).setUsername(username).setPassword(password));
        return "redirect:/v2/users/profile";
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public String userPage(Model model, @PathVariable Long id) throws NotFoundException {
        model.addAttribute("user", userService.findFullInfoById(id));
        model.addAttribute("questions_number", questionService.countQuestionByCreatorId(id));
        return "user";
    }
}
