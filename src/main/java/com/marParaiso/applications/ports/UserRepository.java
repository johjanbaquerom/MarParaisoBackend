package com.marParaiso.applications.ports;

import com.marParaiso.domain.entity.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
}
