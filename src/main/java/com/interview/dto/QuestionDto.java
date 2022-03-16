package com.interview.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QuestionDto {
    private Long id;
    private String text;
    private String answer;
    private float rate;
}
