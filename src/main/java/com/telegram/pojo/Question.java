package com.telegram.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @Getter
    @Setter
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    @Column(name = "text", nullable = false)
    private String text;

    @Getter
    @Setter
    private float rate;

    public Question(String text, float rate) {
        this.text = text;
        this.rate = rate;
    }
}
