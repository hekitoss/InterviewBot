package com.interview.validation;

import com.interview.entity.User;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.time.OffsetDateTime;

@Component
public class UserValidator {

    private static final Logger log = LogManager.getRootLogger();

    @SneakyThrows
    public void validate(User user){
        if(user.getPassword().length() > 255
                || user.getUsername().length() > 20
                || user.getName().length() > 10
                || user.getSurname().length() > 10
                || user.getCreationTime().isAfter(OffsetDateTime.now())
        ) {
            log.debug("user not valid: " + user);
            throw new ValidationException("user not valid");
        }
    }
}
