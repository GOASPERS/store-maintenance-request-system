package com.example.maintenancerequests.repository;

import com.example.maintenancerequests.entity.User;
import com.example.maintenancerequests.enums.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRoleAndActiveTrue(Role role);
}
