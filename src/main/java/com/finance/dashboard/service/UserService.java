package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.UserCreateRequest;
import com.finance.dashboard.dto.request.UserUpdateRequest;
import com.finance.dashboard.dto.response.UserResponse;
import com.finance.dashboard.entity.Role;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.exception.DuplicateResourceException;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.repository.RoleRepository;
import com.finance.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }
        Role role = null;
        if (request.getRoleId() != null) {
            role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", request.getRoleId()));
        }
        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(User.UserStatus.ACTIVE)
                .build();
        user = userRepository.save(user);
        log.info("User created: {}", user.getEmail());
        return mapToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", request.getRoleId()));
            user.setRole(role);
        }
        user = userRepository.save(user);
        log.info("User updated: {}", user.getEmail());
        return mapToUserResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        userRepository.delete(user);
        log.info("User deleted: {}", id);
    }

    @Transactional
    public UserResponse assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
        user.setRole(role);
        user = userRepository.save(user);
        log.info("Role {} assigned to user {}", roleName, userId);
        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        try {
            user.setStatus(User.UserStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new com.finance.dashboard.exception.ValidationException("Invalid status: " + status);
        }
        user = userRepository.save(user);
        log.info("User {} status updated to {}", id, status);
        return mapToUserResponse(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole() != null ? user.getRole().getName() : null)
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
