package com.interview.mapper;

import com.interview.dao.QuestionDao;
import com.interview.dto.QuestionDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {
    private final ModelMapper modelMapper;

    public QuestionMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(QuestionDao.class, QuestionDto.class)
                .addMapping(question -> question.getRate().getAverageRate(), QuestionDto::setRate);
    }

    public QuestionDto convertToDto(QuestionDao questionDao){
        return modelMapper.map(questionDao, QuestionDto.class);
    }
}
