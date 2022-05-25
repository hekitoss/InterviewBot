package com.interview.entity;

import com.interview.entity.constance.Role;
import com.interview.entity.constance.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Accessors(chain = true)
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", allocationSize = 1)
    private Long id;
    private String password;
    @NotBlank
    @Size(min = 3, max = 20, message = "username must be between 3 and 20 characters")
    private String username;
    @NotBlank
    @Size(min = 3, max = 20, message = "name must be between 3 and 20 characters")
    private String name;
    @NotBlank
    @Size(min = 3, max = 20, message = "surname must be between 3 and 20 characters")
    private String surname;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private OffsetDateTime creationTime;
    private OffsetDateTime deletingTime;

    @ManyToMany
    @JoinTable(
            name = "question_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rate_id"))
    private Set<Rate> rates;

    @ManyToMany(mappedBy = "likedUsers")
    private Set<Comment> likedComments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
