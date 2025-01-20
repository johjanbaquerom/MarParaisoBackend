package com.marParaiso.applications.usecase.impl;

import com.marParaiso.applications.ports.RolRepository;
import com.marParaiso.domain.entity.Role;
import com.marParaiso.domain.entity.User;
import com.marParaiso.infrastructure.repository.JpaUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl {

    private final JpaUserRepository userRepository;
    private final RolRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(JpaUserRepository userRepository, RolRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String password, boolean isAdmin) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role role = roleRepository.findByName(isAdmin ? "ROLE_ADMIN" : "ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("El rol no existe."));
        user.setRoles(Set.of(role));

        return userRepository.save(user);
    }
}
