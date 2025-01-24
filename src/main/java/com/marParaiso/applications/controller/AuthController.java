package com.marParaiso.applications.controller;

import com.marParaiso.applications.dto.LoginRequestDto;
import com.marParaiso.applications.dto.RegisterRequestDto;
import com.marParaiso.applications.ports.RolRepository;
import com.marParaiso.applications.ports.UserRepository;
import com.marParaiso.applications.usecase.impl.UserServiceImpl;
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
    private final UserServiceImpl userService;

    public AuthController(@Lazy AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository,
                          RolRepository rolRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          JpaUserRepository jpaUserRepository,
                          UserServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = jpaUserRepository;
        this.userService = userService;

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

        System.out.println("Datos recibidos: " + registerRequest);

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: El nombre de usuario ya está en uso.");
        }

        User newUser = userService.createUser(registerRequest.getUsername(), registerRequest.getPassword());

        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }

}
