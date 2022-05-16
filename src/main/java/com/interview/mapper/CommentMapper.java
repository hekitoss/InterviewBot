package com.interview.mapper;

import com.interview.dto.CommentDto;
import com.interview.entity.Comment;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

@Component
public class CommentMapper {
    private final ModelMapper modelMapper;

    public CommentMapper() {
        this.modelMapper = new ModelMapper();

        TypeMap<Comment, CommentDto> propertyMapper = modelMapper.createTypeMap(Comment.class, CommentDto.class);
        Converter<Collection, Integer> collectionToSize = c -> c.getSource().size();
        propertyMapper.addMappings(
                mapper -> mapper.using(collectionToSize).map(Comment::getLikedUsers, CommentDto::setNumberOfLikes)
        )
                .addMapping(comment -> comment.getCreator().getUsername(), CommentDto::setCreatorUserName)
                .addMapping(comment -> comment.getCreator().getId(), CommentDto::setUserId);
    }

    public CommentDto commentDto(Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }
        return modelMapper.map(comment, CommentDto.class);
    }
}
