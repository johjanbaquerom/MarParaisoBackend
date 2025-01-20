package com.marParaiso.applications.ports;

import com.marParaiso.domain.entity.Role;

import java.util.Optional;

public interface RolRepository {

    Optional<Role> findByName(String name);
}
