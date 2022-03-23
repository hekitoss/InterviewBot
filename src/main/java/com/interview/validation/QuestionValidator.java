package com.interview.validation;

import com.interview.dao.Question;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.time.OffsetDateTime;

@Component
public class QuestionValidator {

    private static final Logger log = LogManager.getRootLogger();

    @SneakyThrows
    public void validate(Question question){
        if(question.getText().length() > 255
                ||question.getAnswer().length() > 255
                ||question.getCreationTime().isAfter(OffsetDateTime.now())) {
            log.error("question not valid: " + question);
            throw new ValidationException("question not valid");
        }
    }
}
