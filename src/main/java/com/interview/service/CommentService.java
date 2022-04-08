package com.interview.service;

import com.interview.dto.CommentDto;
import com.interview.entity.Comment;
import com.interview.entity.User;
import com.interview.exception.NotFoundException;
import com.interview.mapper.CommentMapper;
import com.interview.repository.CommentRepository;
import com.interview.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;
    private final QuestionRepository questionRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, AuthenticationService authenticationService, QuestionRepository questionRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.authenticationService = authenticationService;
        this.questionRepository = questionRepository;
        this.commentMapper = commentMapper;
    }

    public CommentDto findTopCommentByQuestionId(Long id) {
        return null;
    }

    public void likeCommentById(Long id) throws NotFoundException {
        User user = authenticationService.getCurrentUser();
        Set<User> likes = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found comment with id:" + id)).getLikes();
        if(likes.contains(user)) {
            likes.remove(user);
        } else {
            likes.add(user);
        }
    }

    public CommentDto save(Long questionId, String text) {
        return commentMapper.commentDto(
                commentRepository.save(new Comment(text)
                        .setCreator(authenticationService.getCurrentUser())
                        .setQuestion(questionRepository.getById(questionId)))
        );
    }
}
