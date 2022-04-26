package com.interview.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QuestionAndTopCommentDto extends QuestionDto{
    private CommentDto commentDto;
}
