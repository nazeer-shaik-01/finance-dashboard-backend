package com.nazeer.finance.repository;

import com.nazeer.finance.entity.Role;
import com.nazeer.finance.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
