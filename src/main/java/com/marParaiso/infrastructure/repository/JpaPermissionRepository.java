package com.marParaiso.infrastructure.repository;


import com.marParaiso.applications.ports.PermissionRepository;
import com.marParaiso.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPermissionRepository extends PermissionRepository, JpaRepository<Permission, Long> {
}
