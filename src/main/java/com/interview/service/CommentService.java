package com.interview.service;

import com.interview.dto.CommentDto;
import com.interview.entity.Comment;
import com.interview.entity.User;
import com.interview.exception.NotFoundException;
import com.interview.mapper.CommentMapper;
import com.interview.repository.CommentRepository;
import com.interview.repository.QuestionRepository;
import lombok.SneakyThrows;
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

    @SneakyThrows
    public CommentDto findTopCommentByQuestionId(Long questionId) {
        return commentMapper.commentDto(commentRepository.findTopCommentByQuestionId(questionId).orElseThrow(() -> new NotFoundException("Not found comment with id:" + questionId)));
    }

    public CommentDto likeCommentById(Long id) throws NotFoundException {
        User user = authenticationService.getCurrentUser();
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found comment with id:" + id));
        Set<User> likes = comment.getLikes();
        if (likes.contains(user)) {
            likes.remove(user);
            comment.setNumberOfLikes(comment.getNumberOfLikes() - 1);
        } else {
            likes.add(user);
            comment.setNumberOfLikes(comment.getNumberOfLikes() + 1);
        }
        return commentMapper.commentDto(commentRepository.save(comment));
    }

    public CommentDto save(Long questionId, String text) {
        return commentMapper.commentDto(
                commentRepository.save(new Comment(text)
                        .setCreator(authenticationService.getCurrentUser())
                        .setQuestion(questionRepository.getById(questionId)))
        );
    }
}
