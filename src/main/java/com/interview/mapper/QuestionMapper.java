package com.interview.mapper;

import com.interview.dto.QuestionAndAllCommentsDto;
import com.interview.dto.QuestionAndTopCommentDto;
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
                .addMapping(question -> question.getOwner().getId(), QuestionDto::setUserId)
                .addMapping(Question::getCreationTime, QuestionDto::setDate);

        modelMapper.createTypeMap(Question.class, QuestionAndTopCommentDto.class)
                .addMapping(question -> question.getRate().getAverageRate(), QuestionAndTopCommentDto::setRate)
                .addMapping(question -> question.getOwner().getUsername(), QuestionAndTopCommentDto::setUsername)
                .addMapping(question -> question.getOwner().getId(), QuestionAndTopCommentDto::setUserId)
                .addMapping(Question::getCreationTime, QuestionAndTopCommentDto::setDate);

        modelMapper.createTypeMap(Question.class, QuestionAndAllCommentsDto.class)
                .addMapping(question -> question.getRate().getAverageRate(), QuestionAndAllCommentsDto::setRate)
                .addMapping(question -> question.getOwner().getUsername(), QuestionAndAllCommentsDto::setUsername)
                .addMapping(question -> question.getOwner().getId(), QuestionAndAllCommentsDto::setUserId)
                .addMapping(Question::getCreationTime, QuestionAndAllCommentsDto::setDate);
    }

    public QuestionDto convertToDto(Question question){
        return modelMapper.map(question, QuestionDto.class);
    }

    public QuestionAndTopCommentDto convertToDtoWithTopComment(Question question){
        return modelMapper.map(question, QuestionAndTopCommentDto.class);
    }

    public QuestionAndAllCommentsDto convertToDtoWithAllComments(Question question){
        return modelMapper.map(question, QuestionAndAllCommentsDto.class);
    }
}
