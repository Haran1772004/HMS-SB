package com.hospital.hospital_spring.repository;
import java.util.*;
import com.hospital.hospital_spring.entity.User;
import com.hospital.hospital_spring.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByRoleAndStatus(String role,
                                   AccountStatus status);
                                
    void   deleteByUsername(String username);
}