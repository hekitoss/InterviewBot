package com.interview.entity.constance;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import com.interview.entity.constance.Permission.*;

public enum Role {
    ADMIN(Set.of(Permission.QUESTION_DELETE,
            Permission.QUESTIONS_ADD,
            Permission.QUESTIONS_READ,
            Permission.USERS_DELETE,
            Permission.USERS_BAN,
            Permission.USERS_GET_FULL_INFO,
            Permission.USER_ADD,
            Permission.USER_READ)),
    USER(Set.of(Permission.QUESTION_DELETE,
            Permission.QUESTIONS_ADD,
            Permission.QUESTIONS_READ,
            Permission.USER_READ));

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
