package com.interview.service;

import com.interview.dao.Rate;
import com.interview.repository.QuestionRepository;
import com.interview.dao.Question;
import com.interview.repository.RateRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final RateRepository rateRepository;

    public QuestionService(QuestionRepository questionRepository, RateRepository rateRepository) {
        this.questionRepository = questionRepository;
        this.rateRepository = rateRepository;
    }

    public List<Question> findAll() {
        return questionRepository.findAll().stream().filter(q -> !q.isDeleted()).collect(Collectors.toList());
    }

    @SneakyThrows
    public Question delete(Long id){
        return questionRepository.findById(id).stream().filter(q -> !q.isDeleted()).peek(e -> {
            save(e.setDeleted(true));
        }).findFirst().orElseThrow(() -> new Exception("No question with " + id));
    }

    public Question save(Question question) {
        if (Objects.isNull(question.getRate())){
            question.setRate(new Rate());
        }
        rateRepository.save(question.getRate());
        return questionRepository.save(question);
    }

    @SneakyThrows
    public Question evaluate(Long id, int rate){
        return questionRepository.findById(id).stream().filter(q -> !q.isDeleted()).peek(
                e -> rateRepository.save(e.getRate().evaluate(rate)))
                .findFirst().orElseThrow(() -> new Exception("No question with " + id));
    }

    @SneakyThrows
    public Question findById(Long id){
        return questionRepository.findById(id).stream().filter(q -> !q.isDeleted())
                .findFirst().orElseThrow(() -> new Exception("No question with " + id));
    }
}
