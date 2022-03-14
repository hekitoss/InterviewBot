package com.interview.service;

import com.interview.dao.Question;
import com.interview.dao.Rate;
import com.interview.repository.QuestionRepository;
import com.interview.repository.RateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class QuestionServiceTest {
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private RateRepository rateRepository;

    private final Question question;
    private final Question deletedQuestion;
    private final Rate rate;

    @Autowired
    QuestionService questionService;

    {
        question = new Question("text");
        question.setId(1L);
        rate = new Rate();
        question.setRate(rate);
        deletedQuestion = new Question().setDeleted(true);
    }

   @Test
    void findAll() {
        when(questionRepository.findAll()).thenReturn(List.of(question, deletedQuestion));

       List<Question> questions = questionService.findAll();

       Assertions.assertTrue(questions.contains(question));
       Assertions.assertFalse(questions.contains(deletedQuestion));
    }

    @Test
    void delete() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        questionService.delete(1L);

        Assertions.assertTrue(question.isDeleted());
    }

    @Test
    void save() {
        questionService.save(question);

        verify(questionRepository).save(question);
        verify(rateRepository).save(rate);
    }

    @Test
    void evaluate() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        questionService.evaluate(1L, 2);

        Assertions.assertEquals(1, question.getRate().getTwo());
        Assertions.assertEquals(1, question.getRate().getNumberOfEvaluations());
        verify(rateRepository).save(rate);
    }
}