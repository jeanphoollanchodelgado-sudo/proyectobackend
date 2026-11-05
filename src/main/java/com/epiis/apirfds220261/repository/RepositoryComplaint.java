package com.epiis.apirfds220261.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import com.epiis.apirfds220261.entity.EntityComplaint;



import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



@Repository
public interface RepositoryComplaint extends JpaRepository<EntityComplaint, String> {
	EntityComplaint findByCode(String code);
	
	@Query("SELECT c FROM EntityComplaint c " +
	           "WHERE (:idOffice IS NULL OR c.idOffice = :idOffice) " +
	           "AND (:status IS NULL OR c.status = :status)")
	    List<EntityComplaint> findByFilters(
	        @Param("idOffice") String idOffice,
	        @Param("status") String status
	    );
}