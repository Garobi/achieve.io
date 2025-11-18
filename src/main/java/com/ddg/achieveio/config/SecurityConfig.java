package com.ddg.achieveio.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    };

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                    .requestMatchers(HttpMethod.GET,
                            "/games/**",
                            "/platforms/**",
                            "/achievements/**",
                            "/challenges/**",
                            "/finished-achievements/by-user/**",
                            "/finished-achievements/by-achievement/**",
                            "/finished-challenges/by-user/**",
                            "/finished-challenges/by-challenge/**",
                            "/votes/achievements/by-user/**",
                            "/votes/achievements/by-achievement/**",
                            "/votes/achievements/count/**",
                            "/votes/challenges/by-user/**",
                            "/votes/challenges/by-challenge/**",
                            "/votes/challenges/count/**"
                    ).permitAll()
                .requestMatchers(HttpMethod.POST, "/games").hasAnyRole("ADMIN", "OWNER")
                .requestMatchers(HttpMethod.PUT, "/games/**").hasAnyRole("ADMIN", "OWNER")
                .requestMatchers(HttpMethod.DELETE, "/games/**").hasAnyRole("ADMIN", "OWNER")
                .requestMatchers(HttpMethod.POST, "/platforms").hasRole("OWNER")
                .requestMatchers(HttpMethod.PUT, "/platforms/**").hasRole("OWNER")
                .requestMatchers(HttpMethod.DELETE, "/platforms/**").hasRole("OWNER")
                .requestMatchers("/user").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.POST, "/achievements/register").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.PUT, "/achievements/**").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.DELETE, "/achievements/**").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.POST, "/challenges").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.PUT, "/challenges/**").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.DELETE, "/challenges/**").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.GET, "/finished-achievements/me").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.POST, "/finished-achievements").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.DELETE, "/finished-achievements/**").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.GET, "/finished-challenges/me").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.POST, "/finished-challenges").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.DELETE, "/finished-challenges/**").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.GET, "/votes/achievements/me").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.POST, "/votes/achievements").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.DELETE, "/votes/achievements/**").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.GET, "/votes/challenges/me").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.POST, "/votes/challenges").hasAnyRole("USER", "ADMIN", "OWNER")
                .requestMatchers(HttpMethod.DELETE, "/votes/challenges/**").hasAnyRole("USER", "ADMIN", "OWNER")
                .anyRequest().hasRole("OWNER"))
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}