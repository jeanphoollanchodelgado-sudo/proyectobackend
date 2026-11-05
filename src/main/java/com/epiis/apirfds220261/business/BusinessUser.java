package com.epiis.apirfds220261.business;

import java.time.Instant;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.epiis.apirfds220261.controller.JwtService;
import com.epiis.apirfds220261.dto.request.RequestUserLogin;
import com.epiis.apirfds220261.dto.request.RequestUserRegister;
import com.epiis.apirfds220261.dto.response.ResponseUserLogin;
import com.epiis.apirfds220261.dto.response.ResponseUserRegister;
import com.epiis.apirfds220261.entity.EntityUser;
import com.epiis.apirfds220261.repository.RepositoryUser;

@Service
public class BusinessUser {
    private final RepositoryUser repositoryUser;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;

    public BusinessUser(RepositoryUser repositoryUser, JwtService jwtService) {
        this.repositoryUser = repositoryUser;
        this.jwtService = jwtService;
    }

    public ResponseUserRegister register(RequestUserRegister request) {
        ResponseUserRegister response = new ResponseUserRegister();
        String email = request.getEmail().trim();

        if (repositoryUser.existsByEmailIgnoreCase(email)) {
            response.warning();
            response.listMessage.add("Ese correo ya está registrado.");
            return response;
        }

        Instant now = Instant.now();
        EntityUser user = new EntityUser();
        user.setIdUser(UUID.randomUUID().toString());
        user.setFirstName(request.getFirstName().trim());
        user.setSurName(request.getSurName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("Estudiante");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        repositoryUser.save(user);

        response.setIdUser(user.getIdUser());
        response.success();
        response.listMessage.add("Registro realizado correctamente.");
        return response;
    }

    public ResponseUserLogin login(RequestUserLogin request) {
        EntityUser user = repositoryUser
            .findByEmailIgnoreCase(request.getEmail().trim())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Correo o contraseña incorrectos."
            ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Correo o contraseña incorrectos."
            );
        }

        ResponseUserLogin response = new ResponseUserLogin();
        response.setIdUser(user.getIdUser());
        response.setFirstName(user.getFirstName());
        response.setSurName(user.getSurName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setToken(jwtService.generateToken(user));

        return response;
    }
}