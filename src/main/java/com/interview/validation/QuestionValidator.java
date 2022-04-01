package com.interview.validation;

import com.interview.entity.Question;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.time.OffsetDateTime;
import java.util.Objects;

@Component
public class QuestionValidator implements Validator<Question> {

    private static final Logger log = LogManager.getRootLogger();

    @SneakyThrows
    @Override
    public void validate(Question question) {
        if(question.getText().length() > 255
                ||question.getAnswer().length() > 255
                ||question.getCreationTime().isAfter(OffsetDateTime.now())
                ||Objects.isNull(question.getText())
                ||Objects.isNull(question.getAnswer())
                ||Objects.isNull(question.getOwner())
                ||Objects.isNull(question.getRate())) {
            log.error("question not valid: " + question);
            throw new ValidationException("question not valid");
        }
    }
}
