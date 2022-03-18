package com.interview.service;

import com.interview.dao.QuestionDao;
import com.interview.dao.Rate;
import com.interview.dto.QuestionDto;
import com.interview.exception.NotFoundException;
import com.interview.mapper.QuestionMapper;
import com.interview.repository.QuestionRepository;
import com.interview.repository.RateRepository;
import com.interview.validation.QuestionValidator;
import com.interview.validation.RateValidator;
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

    public QuestionService(QuestionRepository questionRepository, RateRepository rateRepository, QuestionMapper questionMapper, RateValidator rateValidator, QuestionValidator questionValidator) {
        this.questionRepository = questionRepository;
        this.rateRepository = rateRepository;
        this.questionMapper = questionMapper;
        this.rateValidator = rateValidator;
        this.questionValidator = questionValidator;
    }

    public List<QuestionDto> findAll() {
        return questionRepository.findAll().stream()
                .filter(q -> !q.isDeleted())
                .map(questionMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public QuestionDao delete(Long id) throws NotFoundException {
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .peek(e -> {save(e.setDeleted(true)
                        .setDeletingTime(OffsetDateTime.now()));})
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No question with id:" + id));
    }

    public QuestionDao save(QuestionDao questionDao) {
        if (Objects.isNull(questionDao.getRate())){
            questionDao.setRate(new Rate());
        }
        rateValidator.validate(questionDao.getRate());
        questionValidator.validate(questionDao);
        rateRepository.save(questionDao.getRate());
        return questionRepository.save(questionDao);
    }

    public QuestionDao evaluate(Long id, int rate) throws NotFoundException {
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .peek(q -> rateValidator.validate(q.getRate()))
                .peek(q -> rateRepository.save(q.getRate().evaluate(rate)))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No question with id:" + id));
    }

    public QuestionDao findById(Long id) throws NotFoundException {
        return questionRepository.findById(id).stream()
                .filter(q -> !q.isDeleted())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No question with id:" + id));
    }
}
