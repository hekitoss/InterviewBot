package com.interview.entity.constance;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.interview.entity.constance.Permission.*;

public enum Role {
    ADMIN(Set.of(
            QUESTIONS_DELETE,
            QUESTIONS_ADD,
            QUESTIONS_READ,
            QUESTIONS_RATE,
            USERS_DELETE,
            USERS_CHANGE_STATUS,
            USERS_GET_FULL_INFO,
            USER_READ,
            COMMENTS_ADD,
            COMMENTS_LIKE

    )),
    USER(Set.of(
            QUESTIONS_DELETE,
            QUESTIONS_ADD,
            QUESTIONS_READ,
            QUESTIONS_RATE,
            USER_READ,
            COMMENTS_ADD,
            COMMENTS_LIKE
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
