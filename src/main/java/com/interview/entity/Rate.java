package com.interview.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.ValidationException;
import java.util.Objects;
import java.util.Set;

@Entity
@Accessors(chain = true)
@Getter
@Setter
@ToString
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rate_seq")
    @SequenceGenerator(name = "rate_seq", allocationSize = 1)
    private Long id;
    private int one;
    private int two;
    private int three;
    private int four;
    private int five;
    private int numberOfEvaluations;
    @ManyToMany(mappedBy = "rates")
    @ToString.Exclude
    private Set<User> users;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Rate rate = (Rate) o;
        return id != null && Objects.equals(id, rate.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
