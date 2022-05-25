package com.interview.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Accessors(chain = true)
@Getter
@Setter
@RequiredArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @SequenceGenerator(name = "comment_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            joinColumns = @JoinColumn(name = "comment_id"))
    private Set<User> likedUsers;

    @NotBlank
    @Size(min = 5, max = 10000, message = "text must be between 5 and 10000 characters")
    private String text;
    private boolean isDeleted;
    private Long numberOfLikes;
    private LocalDateTime creationTime;
    private LocalDateTime deletingTime;

    public Comment(String text) {
        this.text = text;
        this.isDeleted = false;
        this.numberOfLikes = 0L;
        creationTime = LocalDateTime.now();
    }
}
