package com.interview.controller.v2;

import com.interview.exception.NotFoundException;
import com.interview.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v2/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String like(@PathVariable Long id) throws NotFoundException {
        commentService.likeCommentById(id);
        return "redirect:/v2/questions";
    }

    @PostMapping("/{questionId}")
    @PreAuthorize("isAuthenticated()")
    public String commentAdd(@PathVariable Long questionId, @RequestParam String text) {
        commentService.save(questionId, text);
        return "redirect:/v2/questions";
    }
}
