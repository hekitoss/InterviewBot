package com.interview.validation;

import com.interview.dao.QuestionDao;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.time.OffsetDateTime;

@Component
public class QuestionValidator {

    @SneakyThrows
    public void validate(QuestionDao question){
        if(question.getText().length() > 255
                ||question.getAnswer().length() > 255
                ||question.getCreationTime().isAfter(OffsetDateTime.now())) {
            throw new ValidationException("question not valid");
        }
    }
}
