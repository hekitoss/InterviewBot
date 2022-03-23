package com.interview.service;

import com.interview.dao.Question;
import com.interview.dao.Rate;
import com.interview.dto.QuestionDto;
import com.interview.mapper.QuestionMapper;
import com.interview.repository.QuestionRepository;
import com.interview.repository.RateRepository;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceTest {
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private RateRepository rateRepository;
    @MockBean
    private QuestionMapper questionMapper;

    private final Question question;
    private final Question deletedQuestion;
    private final Rate rate;

    @Autowired
    private QuestionService questionService;

    {
        question = new Question("text", "answer");
        question.setId(1L);
        rate = new Rate();
        question.setRate(rate);
        deletedQuestion = new Question().setDeleted(true);
    }

   @Test
   public void findAllMethodCheck() {
       QuestionDto questionDto = new QuestionDto().setId(1L);
       QuestionDto deletedQuestionDto = new QuestionDto().setId(2L);
        when(questionRepository.findAll()).thenReturn(List.of(question, deletedQuestion));
        when(questionMapper.convertToDto(question)).thenReturn(questionDto);
        when(questionMapper.convertToDto(deletedQuestion)).thenReturn(deletedQuestionDto);

       List<QuestionDto> questionDtos = questionService.findAll();

       assertTrue(questionDtos.contains(questionDto));
       assertFalse(questionDtos.contains(deletedQuestionDto));
    }

    @SneakyThrows
    @Test
    public void deleteMethodCheck() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        questionService.deleteById(1L);

        assertTrue(question.isDeleted());
    }

    @Test
    public void saveMethodCheck() {
        questionService.save(question);

        verify(questionRepository).save(question);
        verify(rateRepository).save(rate);
    }

    @SneakyThrows
    @Test
    public void evaluateMethodCheck() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        questionService.evaluateById(1L, 2);

        assertEquals(1, question.getRate().getTwo());
        assertEquals(1, question.getRate().getNumberOfEvaluations());
        verify(rateRepository).save(rate);
    }
}