package com.finance.dashboard.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private Long roleId;
}
