package com.marParaiso.infrastructure.repository;

import com.marParaiso.applications.ports.RolRepository;
import com.marParaiso.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRolRepository extends RolRepository, JpaRepository<Role, Long> {
}
