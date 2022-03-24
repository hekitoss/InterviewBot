package com.interview.dto;

import com.interview.entity.constance.Status;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private Status status;
}
