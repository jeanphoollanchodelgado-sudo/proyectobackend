package com.epiis.apirfds220261.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.epiis.apirfds220261.entity.EntityComplaintComment;

@Repository
public interface RepositoryComplaintComment extends JpaRepository<EntityComplaintComment, String> {
	@Query("select c from EntityComplaintComment c left join fetch c.parentUser where c.idComplaint = :idComplaint order by c.createdAt asc")
	List<EntityComplaintComment> findListByIdComplaint(@Param("idComplaint") String idComplaint);
}