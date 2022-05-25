package com.interview.service;

import com.interview.dto.QuestionAndAllCommentsDto;
import com.interview.dto.QuestionAndTopCommentDto;
import com.interview.dto.QuestionDto;
import com.interview.entity.Question;
import com.interview.entity.Rate;
import com.interview.entity.User;
import com.interview.exception.NotFoundException;
import com.interview.logger.Audit;
import com.interview.mapper.QuestionMapper;
import com.interview.repository.QuestionRepository;
import com.interview.repository.RateRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final RateRepository rateRepository;
    private final QuestionMapper questionMapper;
    private final Validator validator;
    private final AuthenticationService authenticationService;
    private final CommentService commentService;

    public QuestionService(QuestionRepository questionRepository,
                           RateRepository rateRepository,
                           QuestionMapper questionMapper,
                           AuthenticationService authenticationService,
                           CommentService commentService) {
        this.questionRepository = questionRepository;
        this.rateRepository = rateRepository;
        this.questionMapper = questionMapper;
        this.authenticationService = authenticationService;
        this.commentService = commentService;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Audit
    public List<QuestionDto> findAll() {
        log.debug("find all method for questions");
        return questionRepository.findAll().stream()
                .filter(q -> !q.isDeleted())
                .map(questionMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Audit
    public List<QuestionAndTopCommentDto> findAllWithTopComment() {
        log.debug("find all method for questions with comment");
        return questionRepository.findAll().stream()
                .filter(q -> !q.isDeleted())
                .map(questionMapper::convertToDtoWithTopComment)
                .map(questionDto -> questionDto.setCommentDto(commentService.findTopCommentByQuestionId(questionDto.getId())))
                .map(questionDto -> questionDto.setNumberOfComments(commentService.findNumberOfCommentsByQuestionId(questionDto.getId())))
                .collect(Collectors.toList());
    }

    @Audit
    public QuestionDto deleteById(Long id) throws NotFoundException {
        log.debug( "delete question by id method, with id: " + id);
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .peek(e -> save(e.setDeleted(true).setDeletingTime(LocalDateTime.now())))
                .map(questionMapper::convertToDto)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found question with id:" + id));
    }

    @Audit
    public QuestionDto save(Question question) {
        log.debug( "save question method for question: " + question);
        validator.validate(question);
        rateRepository.save(question.getRate());
        return questionMapper.convertToDto(questionRepository.save(question));
    }

    @Audit
    public QuestionDto evaluateById(Long id, int rate) throws NotFoundException {
        log.debug( "evaluate question by id method, with id: " + id);
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .peek(question -> {
                    validator.validate(question.getRate());
                    User currentUser = authenticationService.getCurrentUser();
                    if (question.getRate().getUsers().contains(currentUser)) {
                        throw new IllegalArgumentException("Current user already rated this question");
                    }
                    question.getRate().getUsers().add(currentUser);
                    currentUser.getRates().add(question.getRate());
                    rateRepository.save(question.getRate().evaluate(rate));
                })
                .map(questionMapper::convertToDto)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found question with id:" + id));
    }

    @Audit
    public QuestionAndAllCommentsDto findById(Long id) throws NotFoundException {
        log.debug( "find question by id method, with id: " + id);
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .map(questionMapper::convertToDtoWithAllComments)
                .map(questionDto -> questionDto.setCommentsDto(commentService.findAllCommentsByQuestionId(questionDto.getId())))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found question with id:" + id));
    }

    @Audit
    public QuestionAndAllCommentsDto findRandomQuestion() throws NotFoundException {
        log.debug( "find random question");
        return questionRepository.findRandom()
                .map(questionMapper::convertToDtoWithAllComments)
                .map(questionDto -> questionDto.setCommentsDto(commentService.findAllCommentsByQuestionId(questionDto.getId())))
                .orElseThrow(() -> new NotFoundException("Not found available question"));
    }

    @Audit
    public List<QuestionDto> findAllByUser(User user) {
        log.debug("find all method by user");
        return questionRepository.findAllByOwner(user).stream()
                .filter(q -> !q.isDeleted())
                .map(questionMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Audit
    public int countQuestionByCreatorId(Long id) {
        log.debug("count question number by creator id");
        return questionRepository.countQuestionByOwnerId(id);
    }

    @Audit
    public QuestionAndAllCommentsDto rateById(Long id, int rate) throws NotFoundException {
        log.debug( "rate question by id method, with id: " + id + ", rate: " + rate);
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .map(question -> questionRepository.save(question.setRate(question.getRate().evaluate(rate))))
                .map(questionMapper::convertToDtoWithAllComments)
                .map(questionDto -> questionDto.setCommentsDto(commentService.findAllCommentsByQuestionId(questionDto.getId())))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found question with id:" + id));
    }

    public QuestionDto create(Question question) {
        return save(question.setRate(new Rate()).setOwner(authenticationService.getCurrentUser()));
    }
}
