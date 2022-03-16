package com.interview.mapper;

import com.interview.dao.QuestionDao;
import com.interview.dao.Rate;
import com.interview.dto.QuestionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionMapperTest {

    private final QuestionDao questionDao;

    @Autowired
    private QuestionMapper questionMapper;

    {
        questionDao = new QuestionDao()
                .setText("text")
                .setAnswer("answer")
                .setId(1L)
                .setRate(new Rate()
                        .setId(1L)
                        .setFive(1)
                        .setThree(1)
                        .setNumberOfEvaluations(2)
                );
    }

    @Test
    public void convertToDto(){
        QuestionDto questionDto = questionMapper.convertToDto(questionDao);

        assertEquals(questionDao.getId(), questionDto.getId());
        assertEquals(questionDao.getText(), questionDto.getText());
        assertEquals(questionDao.getRate().getAverageRate(), questionDto.getRate());
    }

}