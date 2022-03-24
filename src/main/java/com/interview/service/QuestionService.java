package com.interview.service;

import com.interview.entity.Question;
import com.interview.entity.Rate;
import com.interview.dto.QuestionDto;
import com.interview.exception.NotFoundException;
import com.interview.mapper.QuestionMapper;
import com.interview.repository.QuestionRepository;
import com.interview.repository.RateRepository;
import com.interview.validation.QuestionValidator;
import com.interview.validation.RateValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final RateRepository rateRepository;
    private final QuestionMapper questionMapper;
    private final RateValidator rateValidator;
    private final QuestionValidator questionValidator;

    private static final Logger log = LogManager.getRootLogger();

    public QuestionService(QuestionRepository questionRepository, RateRepository rateRepository, QuestionMapper questionMapper, RateValidator rateValidator, QuestionValidator questionValidator) {
        this.questionRepository = questionRepository;
        this.rateRepository = rateRepository;
        this.questionMapper = questionMapper;
        this.rateValidator = rateValidator;
        this.questionValidator = questionValidator;
    }

    public List<QuestionDto> findAll() {
        log.debug("find all method for questions");
        return questionRepository.findAll().stream()
                .filter(q -> !q.isDeleted())
                .map(questionMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public Question deleteById(Long id) throws NotFoundException {
        log.debug("delete question by id method, with id: " + id);
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .peek(e -> save(e.setDeleted(true).setDeletingTime(OffsetDateTime.now())))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No question with id:" + id));
    }

    public Question save(Question question) {
        log.debug("save question method");
        if (Objects.isNull(question.getRate())){
            question.setRate(new Rate());
        }
        rateValidator.validate(question.getRate());
        questionValidator.validate(question);
        rateRepository.save(question.getRate());
        return questionRepository.save(question);
    }

    public Question evaluateById(Long id, int rate) throws NotFoundException {
        log.debug("evaluate question by id method, with id: " + id);
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .peek(q -> rateValidator.validate(q.getRate()))
                .peek(q -> rateRepository.save(q.getRate().evaluate(rate)))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No question with id:" + id));
    }

    public Question findById(Long id) throws NotFoundException {
        log.debug("find question by id method, with id: " + id);
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No question with id:" + id));
    }
}
