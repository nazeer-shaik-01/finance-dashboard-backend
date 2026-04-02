package com.nazeer.finance.service;

import com.nazeer.finance.dto.UserRequest;
import com.nazeer.finance.dto.UserResponse;
import com.nazeer.finance.entity.RoleName;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
    UserResponse assignRole(Long id, RoleName roleName);
}
