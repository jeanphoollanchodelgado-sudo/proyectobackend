package com.epiis.apirfds220261.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.epiis.apirfds220261.entity.EntitySuggestionComment;

@Repository
public interface RepositorySuggestionComment extends JpaRepository<EntitySuggestionComment, String> {
	@Query("select c from EntitySuggestionComment c left join fetch c.parentUser where c.idSuggestion = :idSuggestion order by c.createdAt asc")
	List<EntitySuggestionComment> findListByIdSuggestion(@Param("idSuggestion") String idComplaint);
}