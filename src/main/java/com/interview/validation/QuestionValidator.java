package com.interview.validation;

import com.interview.entity.Question;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.time.OffsetDateTime;
import java.util.Objects;

@Component
@Log4j
public class QuestionValidator implements Validator<Question> {

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
