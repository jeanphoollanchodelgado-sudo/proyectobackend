package com.epiis.apirfds220261.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.epiis.apirfds220261.dto.request.RequestAdminFilter;
import com.epiis.apirfds220261.entity.EntityComplaint;
import com.epiis.apirfds220261.entity.EntitySuggestion;
import com.epiis.apirfds220261.repository.RepositoryComplaint;
import com.epiis.apirfds220261.repository.RepositorySuggestion;

@Service
public class BusinessAdmin {

    private final RepositoryComplaint complaintRepository;
    private final RepositorySuggestion suggestionRepository;

    public BusinessAdmin(
        RepositoryComplaint complaintRepository,
        RepositorySuggestion suggestionRepository
    ) {
        this.complaintRepository = complaintRepository;
        this.suggestionRepository = suggestionRepository;
    }

    public List<EntityComplaint> getComplaints(RequestAdminFilter filter) {
        return complaintRepository.findByFilters(
            normalize(filter.getIdOffice()),
            normalize(filter.getStatus())
        );
    }

    public List<EntitySuggestion> getSuggestions(RequestAdminFilter filter) {
        return suggestionRepository.findByFilters(
            normalize(filter.getIdOffice()),
            normalize(filter.getStatus())
        );
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = value.trim();

        if (normalized.equalsIgnoreCase("todos")
            || normalized.equalsIgnoreCase("todas")) {
            return null;
        }

        return normalized;
    }
}