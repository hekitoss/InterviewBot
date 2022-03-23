package com.interview.mapper;

import com.interview.entity.User;
import com.interview.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(User.class, UserDto.class);
    }

    public UserDto convertToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
}
