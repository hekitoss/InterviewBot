package com.interview.entity;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.ValidationException;

@Entity
@Accessors(chain = true)
@Data
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private int one;
    private int two;
    private int three;
    private int four;
    private int five;
    private int numberOfEvaluations;

    public Rate() {
        this.one = 0;
        this.two = 0;
        this.three = 0;
        this.four = 0;
        this.five = 0;
        this.numberOfEvaluations = 0;
    }

    public float getAverageRate(){
        return numberOfEvaluations == 0 ? 0 : (float) (one + two*2 + three*3 + four*4 + five*5) / numberOfEvaluations;
    }

    @SneakyThrows
    public Rate evaluate(int rate) {
        switch (rate) {
            case 1 -> one++;
            case 2 -> two++;
            case 3 -> three++;
            case 4 -> four++;
            case 5 -> five++;
            default -> throw new ValidationException("not correct rating");
        }
        numberOfEvaluations++;
        return this;
    }
}
