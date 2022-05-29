package com.company.Quiz_Tournament.utils;

import com.company.Quiz_Tournament.api.User.User;
import com.company.Quiz_Tournament.configs.UserDetails.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class ContextUtils {

    /**
     * @return spring security context user or empty user if anonymous
     */
    public static Optional<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof CustomUserDetails) {
            return Optional.ofNullable((CustomUserDetails) auth.getPrincipal()).map(CustomUserDetails::getUser);
        }

        return Optional.empty();
    }

    /**
     * @return spring security context user or NoSuchElementException
     */
    public static User getCurrentUserOrThrow() {
        return getCurrentUser().orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No such user"); });
    }

    /**
     * @return true if user is not anonymous
     */
    public static boolean isCurrentUserAuthenticated() {
        return getCurrentUser().isPresent();
    }

    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
