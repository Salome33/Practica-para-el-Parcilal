package com.salo.secure_reactive_api.common.util;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RoleUtils {

    private RoleUtils() {
    }

    public static boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }

        return false;
    }
}