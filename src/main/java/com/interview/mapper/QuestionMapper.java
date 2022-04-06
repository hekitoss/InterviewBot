package com.interview.mapper;

import com.interview.entity.Question;
import com.interview.dto.QuestionDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {
    private final ModelMapper modelMapper;

    public QuestionMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Question.class, QuestionDto.class)
                .addMapping(question -> question.getRate().getAverageRate(), QuestionDto::setRate)
                .addMapping(question -> question.getOwner().getUsername(), QuestionDto::setUsername)
                .addMapping(Question::getCreationTime, QuestionDto::setDate);
    }

    public QuestionDto convertToDto(Question question){
        return modelMapper.map(question, QuestionDto.class);
    }
}
