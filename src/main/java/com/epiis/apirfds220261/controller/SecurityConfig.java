package com.epiis.apirfds220261.controller;

import java.util.List;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    @SuppressWarnings("java:S4502")
    SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtService jwtService,
        CorsConfigurationSource corsConfigurationSource
    ) {
        /*
         * CSRF está desactivado porque esta API autentica con JWT enviado
         * explícitamente en Authorization: Bearer y no usa cookies de sesión.
         * Si se agregan cookies para autenticar, hay que habilitar CSRF.
         */
        try {
            return http
                .cors(cors ->
                    cors.configurationSource(corsConfigurationSource)
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(
                        "/user/register",
                        "/user/login",
                        "/complaint/insert",
                        "/suggestion/insert",
                        "/complaint/getbycode/**",
                        "/suggestion/getbycode/**",
                        "/complaintcomment/insert",
                        "/complaintcomment/getbycode/**",
                        "/suggestioncomment/insert",
                        "/suggestioncomment/getbycode/**"
                    ).permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/admin/office/getall",
                        "/admin/professor/getall"
                    ).permitAll()
                    .requestMatchers(
                        HttpMethod.PATCH,
                        "/admin/complaints/*/status",
                        "/admin/suggestions/*/status"
                    ).hasRole("Encargado")
                    .requestMatchers("/admin/**").hasRole("Encargado")
                    .anyRequest().authenticated()
                )
                .addFilterBefore(
                    new JwtAuthenticationFilter(jwtService),
                    UsernamePasswordAuthenticationFilter.class
                )
                .build();
        } catch (Exception exception) {
            throw new BeanCreationException(
                "No se pudo crear la cadena de filtros de seguridad",
                exception
            );
        }
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
            List.of("http://localhost:4200")
        );
        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );
        configuration.setAllowedHeaders(
            List.of("Authorization", "Content-Type")
        );

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}