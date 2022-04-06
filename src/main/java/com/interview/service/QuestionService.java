package com.interview.service;

import com.interview.dto.QuestionDto;
import com.interview.entity.Question;
import com.interview.entity.Rate;
import com.interview.entity.User;
import com.interview.exception.NotFoundException;
import com.interview.logger.Audit;
import com.interview.mapper.QuestionMapper;
import com.interview.repository.QuestionRepository;
import com.interview.repository.RateRepository;
import com.interview.validation.QuestionValidator;
import com.interview.validation.RateValidator;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final RateRepository rateRepository;
    private final QuestionMapper questionMapper;
    private final RateValidator rateValidator;
    private final QuestionValidator questionValidator;
    private final AuthenticationService authenticationService;


    public QuestionService(QuestionRepository questionRepository, RateRepository rateRepository, QuestionMapper questionMapper, RateValidator rateValidator, QuestionValidator questionValidator, AuthenticationService authenticationService) {
        this.questionRepository = questionRepository;
        this.rateRepository = rateRepository;
        this.questionMapper = questionMapper;
        this.rateValidator = rateValidator;
        this.questionValidator = questionValidator;
        this.authenticationService = authenticationService;
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
        if (Objects.isNull(question.getRate())) {
            question.setRate(new Rate());
        }
        if (Objects.isNull(question.getOwner())) {
            question.setOwner(authenticationService.getCurrentUser());
        }
        rateValidator.validate(question.getRate());
        questionValidator.validate(question);
        rateRepository.save(question.getRate());
        return questionMapper.convertToDto(questionRepository.save(question));
    }

    @Audit
    public QuestionDto evaluateById(Long id, int rate) throws NotFoundException {
        log.debug( "evaluate question by id method, with id: " + id);
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .peek(question -> {
                    rateValidator.validate(question.getRate());
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
    public QuestionDto findById(Long id) throws NotFoundException {
        log.debug( "find question by id method, with id: " + id);
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .map(questionMapper::convertToDto)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Not found question with id:" + id));
    }

    @Audit
    public QuestionDto findRandomQuestion() throws NotFoundException {
        log.debug( "find random question");
        return questionRepository.findRandom()
                .map(questionMapper::convertToDto)
                .orElseThrow(() -> new NotFoundException("Not found available question"));
    }
}
