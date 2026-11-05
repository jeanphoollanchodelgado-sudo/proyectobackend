package com.epiis.apirfds220261.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epiis.apirfds220261.entity.EntityUser;

@Repository
public interface RepositoryUser extends JpaRepository<EntityUser, String> {
    Optional<EntityUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}