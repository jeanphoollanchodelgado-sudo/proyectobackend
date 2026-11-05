package com.epiis.apirfds220261.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.epiis.apirfds220261.entity.EntityComplaintFile;

@Repository
public interface RepositoryComplaintFile extends JpaRepository<EntityComplaintFile, String> {}