package com.interview.controller;

import com.interview.dao.QuestionDao;
import com.interview.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping()
    public ResponseEntity<String> create(@RequestParam String text) {
        questionService.save(new QuestionDao(text));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<QuestionDao>> getAll() {
        return new ResponseEntity<>(questionService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<QuestionDao> rate(@PathVariable long id, @RequestParam int rate) {
        return new ResponseEntity<>(questionService.evaluate(id, rate), HttpStatus.OK);
    }
}
