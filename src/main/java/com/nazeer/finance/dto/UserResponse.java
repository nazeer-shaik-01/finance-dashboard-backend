package com.nazeer.finance.dto;

import java.util.Set;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private boolean active;
    private Set<String> roles;

    public UserResponse(Long id, String username, String email, boolean active, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public boolean isActive() { return active; }
    public Set<String> getRoles() { return roles; }
}
