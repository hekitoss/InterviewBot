package com.interview.entity.constance;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.interview.entity.constance.Permission.*;

public enum Role {
    ADMIN(Set.of(
            QUESTION_DELETE,
            QUESTIONS_ADD,
            QUESTIONS_READ,
            USERS_DELETE,
            USERS_CHANGE_STATUS,
            USERS_GET_FULL_INFO,
            USER_ADD,
            USER_READ
    )),
    USER(Set.of(
            QUESTION_DELETE,
            QUESTIONS_ADD,
            QUESTIONS_READ,
            USER_READ
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}
