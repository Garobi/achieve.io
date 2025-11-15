package com.ddg.achieveio.controller;

import com.ddg.achieveio.config.TokenConfig;
import com.ddg.achieveio.dto.request.LoginRequest;
import com.ddg.achieveio.dto.request.RegisterUserRequest;
import com.ddg.achieveio.dto.response.LoginResponse;
import com.ddg.achieveio.dto.response.RegisterUserResponse;
import com.ddg.achieveio.entity.Role;
import com.ddg.achieveio.entity.RoleEnum;
import com.ddg.achieveio.entity.User;
import com.ddg.achieveio.repository.RoleRepository;
import com.ddg.achieveio.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        
        return new LoginResponse(token);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        Role userRole = findUserRole();

        User user = buildUser(request, userRole);
        User savedUser = userRepository.save(user);

        RegisterUserResponse response = new RegisterUserResponse(savedUser.getName(), savedUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private User buildUser(RegisterUserRequest request, Role role) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(role));
        return user;
    }

    private Role findUserRole() {
        return roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Role ROLE_USER não encontrada"));
    }
}