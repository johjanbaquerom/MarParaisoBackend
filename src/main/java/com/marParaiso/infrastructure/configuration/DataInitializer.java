package com.marParaiso.infrastructure.configuration;

import com.marParaiso.domain.entity.Permission;
import com.marParaiso.domain.entity.Role;
import com.marParaiso.domain.entity.User;
import com.marParaiso.infrastructure.repository.JpaPermissionRepository;
import com.marParaiso.infrastructure.repository.JpaRolRepository;
import com.marParaiso.infrastructure.repository.JpaUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            JpaRolRepository rolRepository,
            JpaPermissionRepository permissionRepository,
            JpaUserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            Permission createProduct = createPermissionIfNotExists("CREATE_PRODUCT", "Permiso para crear productos", permissionRepository);
            Permission updateProduct = createPermissionIfNotExists("UPDATE_PRODUCT", "Permiso para actualizar productos", permissionRepository);
            Permission deleteProduct = createPermissionIfNotExists("DELETE_PRODUCT", "Permiso para eliminar productos", permissionRepository);
            Permission viewProducts = createPermissionIfNotExists("VIEW_PRODUCTS", "Permiso para ver productos", permissionRepository);
            Permission manageUsers = createPermissionIfNotExists("MANAGE_USERS", "Permiso para gestionar usuarios", permissionRepository);
            Permission addToCart = createPermissionIfNotExists("ADD_TO_CART", "Permiso para añadir productos al carrito", permissionRepository);
            Permission makePurchase = createPermissionIfNotExists("MAKE_PURCHASE", "Permiso para realizar compras", permissionRepository);

            Role adminRole = createRoleIfNotExists("ROLE_ADMIN", new HashSet<>(permissionRepository.findAll()), rolRepository);
            Role userRole = createRoleIfNotExists("ROLE_USER", Set.of(viewProducts, addToCart, makePurchase), rolRepository);

            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(adminRole));

                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRoles(Set.of(userRole));

                userRepository.saveAll(Set.of(admin, user));
            }
        };
    }

    private Permission createPermissionIfNotExists(String name, String description, JpaPermissionRepository permissionRepository) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(new Permission(name, description)));
    }

    private Role createRoleIfNotExists(String name, Set<Permission> permissions, JpaRolRepository rolRepository) {
        return rolRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setPermissions(permissions);
                    return rolRepository.save(role);
                });
    }
}

