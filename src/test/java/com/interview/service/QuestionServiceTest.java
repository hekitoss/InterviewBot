package com.interview.service;

import com.interview.dao.QuestionDao;
import com.interview.dao.Rate;
import com.interview.dto.QuestionDto;
import com.interview.mapper.QuestionMapper;
import com.interview.repository.QuestionRepository;
import com.interview.repository.RateRepository;
import lombok.SneakyThrows;
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
    @MockBean
    private QuestionMapper questionMapper;

    private final QuestionDao questionDao;
    private final QuestionDao deletedQuestionDao;
    private final Rate rate;

    @Autowired
    private QuestionService questionService;

    {
        questionDao = new QuestionDao("text", "answer");
        questionDao.setId(1L);
        rate = new Rate();
        questionDao.setRate(rate);
        deletedQuestionDao = new QuestionDao().setDeleted(true);
    }

   @Test
    void findAll() {
       QuestionDto questionDto = new QuestionDto().setId(1L);
       QuestionDto deletedQuestionDto = new QuestionDto().setId(2L);
        when(questionRepository.findAll()).thenReturn(List.of(questionDao, deletedQuestionDao));
        when(questionMapper.convertToDto(questionDao)).thenReturn(questionDto);
        when(questionMapper.convertToDto(deletedQuestionDao)).thenReturn(deletedQuestionDto);

       List<QuestionDto> questionDtos = questionService.findAll();

       Assertions.assertTrue(questionDtos.contains(questionDto));
       Assertions.assertFalse(questionDtos.contains(deletedQuestionDto));
    }

    @SneakyThrows
    @Test
    void delete() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(questionDao));

        questionService.delete(1L);

        Assertions.assertTrue(questionDao.isDeleted());
    }

    @Test
    void save() {
        questionService.save(questionDao);

        verify(questionRepository).save(questionDao);
        verify(rateRepository).save(rate);
    }

    @SneakyThrows
    @Test
    void evaluate() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(questionDao));

        questionService.evaluate(1L, 2);

        Assertions.assertEquals(1, questionDao.getRate().getTwo());
        Assertions.assertEquals(1, questionDao.getRate().getNumberOfEvaluations());
        verify(rateRepository).save(rate);
    }
}