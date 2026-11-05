package com.epiis.apirfds220261.business;

import java.util.HashMap;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.epiis.apirfds220261.dto.response.ResponseOfficeGetAll;
import com.epiis.apirfds220261.entity.EntityOffice;
import com.epiis.apirfds220261.repository.RepositoryOffice;
import com.epiis.apirfds220261.dto.request.RequestOfficeInsert;

@Service
public class BusinessOffice {
	private final RepositoryOffice repositoryOffice;

	public BusinessOffice(RepositoryOffice repositoryOffice) {
		this.repositoryOffice = repositoryOffice;
	}
	
	public ResponseOfficeGetAll getAll() {
		ResponseOfficeGetAll response = new ResponseOfficeGetAll();
		
		List<EntityOffice> listEntityOffice = repositoryOffice.findAll();
		
		for(EntityOffice item: listEntityOffice) {
			Map<String, String> data = new HashMap<>();
			
			data.put("idOffice", item.getIdOffice());
			data.put("name", item.getName());
			
			response.getListOffice().add(data);
		}
		
		response.success();
		
		return response;
	}
	public ResponseOfficeGetAll insert(RequestOfficeInsert request) {
		ResponseOfficeGetAll response =new ResponseOfficeGetAll();
		
		String name =request.getName() == null ? "" : request.getName().trim();
		if(name.isEmpty()) {
			response.error();
			response.getListMessage().add("El nombre de la oficina es obligatorio .");
			return response;
		}
		
		boolean exists =repositoryOffice.findAll()
				.stream()
				.anyMatch(item ->item.getName() != null
				&& item.getName().equalsIgnoreCase(name));
		
		if (exists) {
            response.warning();
            response.getListMessage().add("La oficina ya existe.");
            return response;
        }
		
		EntityOffice entity = new  EntityOffice();
		entity.setIdOffice(UUID.randomUUID().toString());
		entity.setName(name);
		Instant now = Instant.now();
		entity.setCreatedAt(now);
		entity.setUpdatedAt(now);
		
		repositoryOffice.save(entity);
		response.success();
		response.getListMessage().add("oficina registrada correctamente");
		return response;
	}
}