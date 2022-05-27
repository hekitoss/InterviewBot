package com.interview.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;


@Entity
@Accessors(chain = true)
@Getter
@Setter
@RequiredArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_seq")
    @SequenceGenerator(name = "question_seq", allocationSize = 1)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question")
    private Set<Rate> rates;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private User owner;

    @NotBlank
    @Size(min = 5, max = 100, message = "question name must be between 5 and 100 characters")
    private String questionName;

    @NotBlank
    @Size(min = 5, max = 10000, message = "text must be between 5 and 10000 characters")
    private String text;

    @NotBlank
    @Size(min = 5, max = 10000, message = "answer must be between 5 and 10000 characters")
    private String answer;

    private double averageRate;
    private boolean isDeleted;
    private LocalDateTime creationTime;
    private LocalDateTime deletingTime;


    public Question(String text, String answer, String questionName) {
        this.text = text;
        this.answer = answer;
        this.questionName = questionName;
        this.creationTime = LocalDateTime.now();
        this.isDeleted = false;
        this.averageRate = 0.0;
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

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionName=" + questionName +
                ", text='" + text + '\'' +
                ", answer='" + answer + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
