package com.interview.controller.v1;

import com.interview.dto.CommentDto;
import com.interview.exception.NotFoundException;
import com.interview.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> like(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(commentService.likeCommentById(id), HttpStatus.OK);
    }

    @GetMapping("/{questionId}/top")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> findTopComment(@PathVariable Long questionId) {
        return new ResponseEntity<>(commentService.findTopCommentByQuestionId(questionId), HttpStatus.OK);
    }

    @PostMapping("/{questionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> commentAdd(@PathVariable Long questionId, @RequestParam String text) {
        return new ResponseEntity<>(commentService.create(questionId, text), HttpStatus.OK);
    }
}
