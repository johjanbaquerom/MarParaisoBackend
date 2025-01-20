package com.marParaiso.applications.controller;

import com.marParaiso.applications.dto.LoginRequestDto;
import com.marParaiso.applications.dto.RegisterRequestDto;
import com.marParaiso.applications.ports.RolRepository;
import com.marParaiso.applications.ports.UserRepository;
import com.marParaiso.domain.entity.Role;
import com.marParaiso.domain.entity.User;
import com.marParaiso.infrastructure.configuration.JwtTokenProvider;
import com.marParaiso.infrastructure.repository.JpaUserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JpaUserRepository userRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(@Lazy AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository,
                          RolRepository rolRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          JpaUserRepository jpaUserRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = jpaUserRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String token = jwtTokenProvider.createToken(authentication.getName());
        return ResponseEntity.ok().body(Collections.singletonMap("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: El nombre de usuario ya está en uso.");
        }

        if (registerRequest.getRole() == null || registerRequest.getRole().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El rol no puede estar vacío.");
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Role userRole = rolRepository.findByName(registerRequest.getRole())
                .orElseThrow(() -> new IllegalArgumentException("Error: El rol no existe."));

        newUser.setRoles(Collections.singleton(userRole));

        userRepository.save(newUser);

        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }
}
