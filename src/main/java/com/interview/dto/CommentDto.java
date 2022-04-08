package com.interview.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String text;
    private String creatorUserName;
    private Long userId;
}
