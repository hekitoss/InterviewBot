package com.interview.dto;

import com.interview.entity.constance.Status;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private Status status;
}
