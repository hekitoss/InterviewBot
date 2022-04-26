package com.interview.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class QuestionAndAllCommentsDto extends QuestionDto{
    private List<CommentDto> commentsDto;
}
