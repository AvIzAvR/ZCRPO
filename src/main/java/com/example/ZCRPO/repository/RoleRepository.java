package com.example.ZCRPO.repository;

import com.example.ZCRPO.model.role.Role;
import com.example.ZCRPO.model.role.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum roleName);

}