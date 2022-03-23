package com.interview.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.OffsetDateTime;
import java.util.Objects;


@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Data
@ToString
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rate_id")
    private Rate rate;

    @NotNull
    private String text;
    @NotNull
    private String answer;
    @NotNull
    private boolean isDeleted;
    @NotNull
    private OffsetDateTime creationTime;
    private OffsetDateTime deletingTime;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private User owner;

    public Question(String text, String answer) {
        this.text = text;
        this.answer = answer;
        creationTime = OffsetDateTime.now();
        this.isDeleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Question question = (Question) o;
        return id != null && Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
