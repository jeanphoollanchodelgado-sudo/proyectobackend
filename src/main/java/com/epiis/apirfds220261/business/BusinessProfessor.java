package com.epiis.apirfds220261.business;

import java.util.HashMap;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.epiis.apirfds220261.dto.response.ResponseProfessorGetAll;
import com.epiis.apirfds220261.entity.EntityProfessor;
import com.epiis.apirfds220261.repository.RepositoryProfessor;
import com.epiis.apirfds220261.dto.request.RequestProfessorInsert;

@Service
public class BusinessProfessor {
	private final RepositoryProfessor repositoryProfessor;

	public BusinessProfessor(
			RepositoryProfessor repositoryProfessor
	) {
		this.repositoryProfessor = repositoryProfessor;
	}
	
	public ResponseProfessorGetAll getAll() {
		ResponseProfessorGetAll response = new ResponseProfessorGetAll();
		
		List<EntityProfessor> listEntityProfessor = repositoryProfessor.findAll();
		
		for(EntityProfessor item: listEntityProfessor) {
			Map<String, String> data = new HashMap<>();
			
			data.put("idProfessor", item.getIdProfessor());
			data.put("firstName", item.getFirstName());
			data.put("surName", item.getSurName());
			data.put("email", item.getEmail());
			
			response.getListProfessor().add(data);
		}
		
		response.success();
		
		return response;
	}
	
	public ResponseProfessorGetAll insert(RequestProfessorInsert request) {
        ResponseProfessorGetAll response = new ResponseProfessorGetAll();

        String firstName = request.getFirstName() == null ? "" : request.getFirstName().trim();
        String surName = request.getSurName() == null ? "" : request.getSurName().trim();
        String email = request.getEmail() == null ? "" : request.getEmail().trim();

        if (firstName.isEmpty() || surName.isEmpty() || email.isEmpty()) {
            response.error();
            response.getListMessage().add("Todos los campos son obligatorios.");
            return response;
        }

        boolean exists = repositoryProfessor.findAll()
                .stream()
                .anyMatch(item ->
                        item.getFirstName() != null && item.getSurName() != null &&
                        item.getFirstName().equalsIgnoreCase(firstName) &&
                        item.getSurName().equalsIgnoreCase(surName));

        if (exists) {
            response.warning();
            response.getListMessage().add("El docente ya existe.");
            return response;
        }

        EntityProfessor entity = new EntityProfessor();
        entity.setIdProfessor(UUID.randomUUID().toString());
        entity.setFirstName(firstName);
        entity.setSurName(surName);
        entity.setEmail(email);
        Instant now = Instant.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        repositoryProfessor.save(entity);

        response.success();
        response.getListMessage().add("Docente registrado correctamente.");
        return response;
    }
	
}