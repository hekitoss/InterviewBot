package com.interview.dao;

import com.interview.config.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String password;
    private String username;
    private String name;
    private String surname;
    private Role role;
    private boolean isDeleted;
    private OffsetDateTime creationTime;
    private OffsetDateTime deletingTime;


}
