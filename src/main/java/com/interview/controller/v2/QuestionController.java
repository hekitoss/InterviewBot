package com.interview.controller.v2;

import com.interview.entity.Question;
import com.interview.service.QuestionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/v2/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "questions";
    }

    @GetMapping("/add")
    public String questionAdd(Model model) {
        return "questions add";
    }

    @PostMapping("/add")
    public String questionAdd(@RequestParam String text, @RequestParam String answer, @RequestParam String questions_name) {
        questionService.save(new Question(text, answer, questions_name));
        return "redirect:/v2/questions";
    }
}
