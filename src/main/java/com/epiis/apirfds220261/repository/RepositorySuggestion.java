package com.epiis.apirfds220261.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.epiis.apirfds220261.entity.EntitySuggestion;




import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;




@Repository
public interface RepositorySuggestion extends JpaRepository<EntitySuggestion, String> {
	EntitySuggestion findByCode(String code);
	
	
	 @Query("SELECT s FROM EntitySuggestion s " +
	           "WHERE (:idOffice IS NULL OR s.idOffice = :idOffice) " +
	           "AND (:status IS NULL OR s.status = :status)")
	    List<EntitySuggestion> findByFilters(
	        @Param("idOffice") String idOffice,
	        @Param("status") String status
	    );
}

