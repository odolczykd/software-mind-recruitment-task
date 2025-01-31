package com.softwaremind.odolczykd.recruitment.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_CREDENTIALS = "user";
    private static final String ADMIN_CREDENTIALS = "admin";
    private static final String AUTH_PATH = "/auth/**";
    private static final String PRODUCTS_PATH = "/products";
    private static final String PRODUCTS_ID_PATH = "/products/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_PATH).permitAll()
                        .requestMatchers(HttpMethod.POST, PRODUCTS_PATH).hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, PRODUCTS_ID_PATH).hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, PRODUCTS_ID_PATH).hasRole(ADMIN_ROLE)
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        var manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(USER_CREDENTIALS)
                .password(passwordEncoder().encode(USER_CREDENTIALS))
                .roles(USER_ROLE)
                .build());
        manager.createUser(User.withUsername(ADMIN_CREDENTIALS)
                .password(passwordEncoder().encode(ADMIN_CREDENTIALS))
                .roles(USER_ROLE, ADMIN_ROLE)
                .build());
        return manager;
    }
}
