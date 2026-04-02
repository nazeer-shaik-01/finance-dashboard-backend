package com.nazeer.finance.dto;

import com.nazeer.finance.entity.RoleName;
import jakarta.validation.constraints.NotNull;

public class AssignRoleRequest {
    @NotNull
    private RoleName role;

    public RoleName getRole() { return role; }
    public void setRole(RoleName role) { this.role = role; }
}
