package com.hospital.hospital_spring.model;

import com.hospital.hospital_spring.entity.User;

public class UserResponse {

    private int userId;
    private String username;
    private String role;
    private String status;

    public UserResponse() {
    }

    public UserResponse(int userId, String username, String role, String status) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.status = status;
    }

    public static UserResponse fromEntity(User user) {
        if (user == null) return null;
        return new UserResponse(
                user.takeUserId(),
                user.takeUsername(),
                user.takeRole(),
                user.takeStatus() != null ? user.takeStatus().name() : null
        );
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
