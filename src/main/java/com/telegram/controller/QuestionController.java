package com.telegram.controller;

import com.telegram.QuestionRepository;
import com.telegram.pojo.Question;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    final QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @PostMapping()
    public String create(@RequestParam String text) {
        questionRepository.save(new Question(text, 0));
        return "успех";
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Question> create() {
        return questionRepository.findAll();
    }
}
