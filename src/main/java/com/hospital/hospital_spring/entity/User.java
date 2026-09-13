package com.hospital.hospital_spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hospital.hospital_spring.model.AccountStatus;
import jakarta.persistence.*;



@Entity
@Table(name = "users")
@JsonPropertyOrder({
    "userId",
    "username",
    "password",
    "role",
    "status",
    "statusName"
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;


    
    public User() {
    }


    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = AccountStatus.ACTIVE;
    }


    public User(String username, String password, String role,
                int userId, AccountStatus status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.userId = userId;
        this.status = status;
    }


    @JsonProperty("userId")
    public int takeUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    @JsonProperty("username")
    public String takeUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @JsonProperty("password")
    public String takePassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @JsonProperty("role")
    public String takeRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    @JsonProperty("status")
    public AccountStatus takeStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }


    @JsonProperty("statusName")
    public String takeStatusName() {
        return status == null ? null : status.name();
    }
}