package com.interview.controller.v1;

import com.interview.entity.Question;
import com.interview.dto.QuestionDto;
import com.interview.exception.NotFoundException;
import com.interview.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionRestController {
    private final QuestionService questionService;

    public QuestionRestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('QUESTIONS_ADD')")
    public ResponseEntity<QuestionDto> create(@RequestParam String text, @RequestParam String answer, @RequestParam String questionName) {
        return new ResponseEntity<>(questionService.create(new Question(text, answer, questionName)), HttpStatus.CREATED);
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('QUESTIONS_READ')")
    public ResponseEntity<List<QuestionDto>> getAll() {
        return new ResponseEntity<>(questionService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/{id}/rate")
    @PreAuthorize("hasAnyAuthority('QUESTIONS_RATE')")
    public ResponseEntity<QuestionDto> rate(@PathVariable long id, @RequestParam int rate) throws NotFoundException {
        return new ResponseEntity<>(questionService.rateById(id, rate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('QUESTIONS_DELETE')")
    public ResponseEntity<QuestionDto> delete(@PathVariable long id) throws NotFoundException {
        return new ResponseEntity<>(questionService.deleteById(id), HttpStatus.OK);
    }

    @GetMapping("/random")
    @PreAuthorize("hasAnyAuthority('QUESTIONS_READ')")
    public ResponseEntity<QuestionDto> getRandomQuestions() throws NotFoundException {
        return new ResponseEntity<>(questionService.findRandomQuestion(), HttpStatus.OK);
    }
}
