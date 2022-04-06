package com.interview.service;

import com.interview.entity.Question;
import com.interview.entity.Rate;
import com.interview.dto.QuestionDto;
import com.interview.entity.User;
import com.interview.mapper.QuestionMapper;
import com.interview.repository.QuestionRepository;
import com.interview.repository.RateRepository;
import com.interview.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationService authenticationService;

    private Question question;
    private Question deletedQuestion;
    private Rate rate;
    private User admin;
    private final Set<Rate> setsOfRates = new HashSet<>();

    @Autowired
    private QuestionService questionService;

    @Before
    public void setUp() {
        admin = new User()
                .setUsername("admin");
        question = new Question("text", "answer", "name");
        question.setId(1L);
        Set<User> setsOfUsers = new HashSet<User>();
        setsOfUsers.add(admin);
        rate = new Rate();
        setsOfRates.add(rate);
        rate.setUsers(setsOfUsers);
        admin.setRates(setsOfRates);
        question.setRate(rate);
        deletedQuestion = new Question().setDeleted(true);
        when(userRepository.findUserByUsername("admin")).thenReturn(Optional.of(new User()));
    }

   @Test
   @WithMockUser(roles = "ADMIN", username = "admin")
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
    @WithMockUser(roles = "ADMIN", username = "admin")
    public void deleteMethodCheck() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(questionMapper.convertToDto(question)).thenReturn(new QuestionDto());

        questionService.deleteById(1L);

        assertTrue(question.isDeleted());
    }

    @Test
    @WithMockUser(value = "ADMIN", username = "admin")
    public void saveMethodCheck() {
        questionService.save(question);

        verify(questionRepository).save(question);
        verify(rateRepository).save(rate);
    }

    @SneakyThrows
    @Test
    @WithMockUser(roles = "ADMIN", username = "admin")
    public void evaluateMethodCheck() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(questionMapper.convertToDto(question)).thenReturn(new QuestionDto());
        when(authenticationService.getCurrentUser()).thenReturn(new User().setRates(setsOfRates));

        questionService.evaluateById(1L, 2);

        assertEquals(1, question.getRate().getTwo());
        assertEquals(1, question.getRate().getNumberOfEvaluations());
        verify(rateRepository).save(rate);
    }
}