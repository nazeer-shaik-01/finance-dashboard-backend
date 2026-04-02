package com.finance.dashboard.security;

import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("securityUtils")
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return userRepository.findByEmail(authentication.getName());
    }

    public Long getCurrentUserId() {
        return getCurrentUser().map(User::getId).orElse(null);
    }

    public boolean isCurrentUser(Long userId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(userId);
    }

    public boolean hasRole(String roleName) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + roleName));
    }
}
