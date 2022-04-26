package com.interview.service;

import com.interview.dto.CommentDto;
import com.interview.entity.Comment;
import com.interview.entity.User;
import com.interview.exception.NotFoundException;
import com.interview.logger.Audit;
import com.interview.mapper.CommentMapper;
import com.interview.repository.CommentRepository;
import com.interview.repository.QuestionRepository;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;
    private final QuestionRepository questionRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository,
                          AuthenticationService authenticationService,
                          QuestionRepository questionRepository,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.authenticationService = authenticationService;
        this.questionRepository = questionRepository;
        this.commentMapper = commentMapper;
    }

    @Audit
    @SneakyThrows
    public CommentDto findTopCommentByQuestionId(Long questionId) {
        log.debug("find top comment method for question with id: " + questionId);
        return commentMapper.commentDto(commentRepository.findTopCommentByQuestionId(questionId));
    }

    @Audit
    @SneakyThrows
    public List<CommentDto> findAllCommentsByQuestionId(Long questionId) {
        log.debug("find all comments method for question with id: " + questionId);
        return commentRepository.findAllByQuestionId(questionId).stream()
                .filter(q -> !q.isDeleted())
                .map(commentMapper::commentDto)
                .collect(Collectors.toList());
    }

    @Audit
    public CommentDto likeCommentById(Long id) throws NotFoundException {
        log.debug("like comment method with id: " + id);
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

    @Audit
    public CommentDto save(Long questionId, String text) {
        log.debug("save comment method for question with id: " + questionId + ", comment: " + text);
        return commentMapper.commentDto(
                commentRepository.save(new Comment(text)
                        .setCreator(authenticationService.getCurrentUser())
                        .setQuestion(questionRepository.getById(questionId)))
        );
    }
}
