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
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final RoleRepository roleRepository;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest registerUserRequest){

        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        User newUser = new User();
        newUser.setName(registerUserRequest.name());
        newUser.setEmail(registerUserRequest.email());
        newUser.setPassword(passwordEncoder.encode(registerUserRequest.password()));
        newUser.setRoles(Set.of(userRole));
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(newUser.getName(), newUser.getEmail()));
    }

}
