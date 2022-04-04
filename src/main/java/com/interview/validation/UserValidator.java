package com.interview.validation;

import com.interview.entity.User;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.time.OffsetDateTime;

@Component
@Log4j
public class UserValidator implements Validator<User> {

    @SneakyThrows
    @Override
    public void validate(User user){
        if(user.getPassword().length() > 255
                || user.getUsername().length() > 20
                || user.getName().length() > 10
                || user.getSurname().length() > 10
                || user.getCreationTime().isAfter(OffsetDateTime.now())
        ) {
            log.error("user not valid: " + user);
            throw new ValidationException("user not valid");
        }
    }
}
