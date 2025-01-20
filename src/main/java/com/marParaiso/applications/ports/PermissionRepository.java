package com.marParaiso.applications.ports;

import com.marParaiso.domain.entity.Permission;

import java.util.Optional;

public interface PermissionRepository {

    Optional<Permission> findByName(String name);
}
