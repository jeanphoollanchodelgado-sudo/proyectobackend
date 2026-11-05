package com.epiis.apirfds220261.controller;

import java.time.Instant;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.apirfds220261.business.BusinessAdmin;
import com.epiis.apirfds220261.dto.request.RequestAdminFilter;
import com.epiis.apirfds220261.entity.EntityComplaint;
import com.epiis.apirfds220261.entity.EntitySuggestion;
import com.epiis.apirfds220261.repository.RepositoryComplaint;
import com.epiis.apirfds220261.repository.RepositorySuggestion;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final BusinessAdmin businessAdmin;
    private final RepositoryComplaint complaintRepository;
    private final RepositorySuggestion suggestionRepository;

    public AdminController(
            BusinessAdmin businessAdmin,
            RepositoryComplaint complaintRepository,
            RepositorySuggestion suggestionRepository
    ) {
        this.businessAdmin = businessAdmin;
        this.complaintRepository = complaintRepository;
        this.suggestionRepository = suggestionRepository;
    }

    @GetMapping("/complaints")
    public List<EntityComplaint> getComplaints(
            @ModelAttribute RequestAdminFilter filter
    ) {
        return businessAdmin.getComplaints(filter);
    }

    @GetMapping("/suggestions")
    public List<EntitySuggestion> getSuggestions(
            @ModelAttribute RequestAdminFilter filter
    ) {
        return businessAdmin.getSuggestions(filter);
    }

    @PatchMapping("/complaints/{id}/status")
    public ResponseEntity<Void> updateComplaintStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        String status = body.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return complaintRepository.findById(id).map(complaint -> {
            complaint.setStatus(status);
            complaint.setUpdatedAt(Instant.now());
            complaintRepository.save(complaint);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/suggestions/{id}/status")
    public ResponseEntity<Void> updateSuggestionStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        String status = body.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return suggestionRepository.findById(id).map(suggestion -> {
            suggestion.setStatus(status);
            suggestion.setUpdatedAt(Instant.now());
            suggestionRepository.save(suggestion);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}