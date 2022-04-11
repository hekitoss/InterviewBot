package com.interview.controller.v2;

import com.interview.dto.QuestionDto;
import com.interview.entity.Question;
import com.interview.exception.NotFoundException;
import com.interview.service.CommentService;
import com.interview.service.QuestionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/v2/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping()
    @PreAuthorize("permitAll()")
    public String getAll(Model model) {
        List<QuestionDto> all = questionService.findAllWithComment();
        model.addAttribute("questions", all);
        return "questions";
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public String getQuestionsById(@PathVariable Long id, Model model) throws NotFoundException {
        model.addAttribute("question", questionService.findById(id));
        return "question";
    }

    @GetMapping("/random")
    @PreAuthorize("permitAll()")
    public String getRandomQuestions(Model model) throws NotFoundException {
        model.addAttribute("question", questionService.findRandomQuestion());
        return "question";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('QUESTIONS_ADD')")
    public String questionAdd() {
        return "questions add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('QUESTIONS_ADD')")
    public String questionAdd(@RequestParam String text, @RequestParam String answer, @RequestParam String questions_name) {
        questionService.save(new Question(text, answer, questions_name));
        return "redirect:/v2/questions";
    }
}
