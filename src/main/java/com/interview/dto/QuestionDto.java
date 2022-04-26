package com.interview.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class QuestionDto {
    private Long id;
    private String text;
    private String answer;
    private String username;
    private Long userId;
    private String questionName;
    private float rate;
    private LocalDateTime date;
}
