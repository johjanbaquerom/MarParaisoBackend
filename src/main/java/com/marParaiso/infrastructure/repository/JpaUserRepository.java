package com.marParaiso.infrastructure.repository;

import com.marParaiso.applications.ports.UserRepository;
import com.marParaiso.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
}
