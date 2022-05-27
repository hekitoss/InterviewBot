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
import java.util.stream.IntStream;

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
        return questionMapper.convertToDto(questionRepository.save(question));
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
    public QuestionAndAllCommentsDto rateById(Long questionId, int rateNumber) throws NotFoundException {
        log.debug( "evaluate question by id method, with id: " + questionId);
        return questionRepository.findById(questionId).stream()
                .filter(q -> !q.isDeleted())
                .peek(question -> {
                    Rate rate = new Rate()
                            .setRating(rateNumber)
                            .setQuestion(question)
                            .setUser(authenticationService.getCurrentUser());

                    validator.validate(rate);
                    deleteRateByQuestion(question);
                    rateRepository.save(rate);
                    updateRating(question);
                })
                .map(questionMapper::convertToDtoWithAllComments)
                .map(questionDto -> questionDto.setCommentsDto(commentService.findAllCommentsByQuestionId(questionDto.getId())))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found question with id:" + questionId));
    }

    private void updateRating(Question question) {
        question.setAverageRate(rateRepository.findAllByQuestionId(question.getId()).stream()
                .flatMapToInt(rateFromAll -> IntStream.of(rateFromAll.getRating()))
                .average().orElseGet(() -> 0.0f));

        questionRepository.save(question);
    }

    public QuestionDto create(Question question) {
        return save(question.setOwner(authenticationService.getCurrentUser())
                .setCreationTime(LocalDateTime.now()));
    }

    private void deleteRateByQuestion(Question question) {
        rateRepository.findRateByUserIdAndQuestionId(authenticationService.getCurrentUser().getId(), question.getId())
                .ifPresent(oldRate -> {
                    rateRepository.delete(oldRate);
                    question.getRates().remove(oldRate);
                });
    }
}
