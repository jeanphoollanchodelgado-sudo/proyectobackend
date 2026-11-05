package com.epiis.apirfds220261.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.epiis.apirfds220261.business.BusinessAdmin;
import com.epiis.apirfds220261.dto.request.RequestAdminFilter;
import com.epiis.apirfds220261.entity.EntityComplaint;
import com.epiis.apirfds220261.entity.EntitySuggestion;
import com.epiis.apirfds220261.repository.RepositoryComplaint;
import com.epiis.apirfds220261.repository.RepositorySuggestion;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private BusinessAdmin businessAdmin;
    @Mock
    private RepositoryComplaint complaintRepository;
    @Mock
    private RepositorySuggestion suggestionRepository;

    @InjectMocks
    private AdminController controller;

    @Test
    void delegatesListingRequests() {
        RequestAdminFilter filter = new RequestAdminFilter();
        when(businessAdmin.getComplaints(filter)).thenReturn(java.util.List.of());
        when(businessAdmin.getSuggestions(filter)).thenReturn(java.util.List.of());
        assertEquals(java.util.List.of(), controller.getComplaints(filter));
        assertEquals(java.util.List.of(), controller.getSuggestions(filter));
    }

    @Test
    void updatesComplaintStatusAndReturnsValidationAndNotFoundStatuses() {
        assertEquals(
            HttpStatus.BAD_REQUEST,
            controller.updateComplaintStatus("id", Map.of("status", " ")).getStatusCode()
        );
        assertEquals(
            HttpStatus.BAD_REQUEST,
            controller.updateComplaintStatus("id", Map.of()).getStatusCode()
        );
        when(complaintRepository.findById("missing")).thenReturn(Optional.empty());
        assertEquals(
            HttpStatus.NOT_FOUND,
            controller.updateComplaintStatus("missing", Map.of("status", "Seen"))
                .getStatusCode()
        );

        EntityComplaint complaint = new EntityComplaint();
        when(complaintRepository.findById("id")).thenReturn(Optional.of(complaint));
        assertEquals(
            HttpStatus.NO_CONTENT,
            controller.updateComplaintStatus("id", Map.of("status", "Seen"))
                .getStatusCode()
        );
        assertEquals("Seen", complaint.getStatus());
        verify(complaintRepository).save(complaint);
    }

    @Test
    void updatesSuggestionStatusAndReturnsValidationAndNotFoundStatuses() {
        assertEquals(
            HttpStatus.BAD_REQUEST,
            controller.updateSuggestionStatus("id", Map.of("status", " ")).getStatusCode()
        );
        assertEquals(
            HttpStatus.BAD_REQUEST,
            controller.updateSuggestionStatus("id", Map.of()).getStatusCode()
        );
        when(suggestionRepository.findById("missing")).thenReturn(Optional.empty());
        assertEquals(
            HttpStatus.NOT_FOUND,
            controller.updateSuggestionStatus("missing", Map.of("status", "Seen"))
                .getStatusCode()
        );

        EntitySuggestion suggestion = new EntitySuggestion();
        when(suggestionRepository.findById("id")).thenReturn(Optional.of(suggestion));
        assertEquals(
            HttpStatus.NO_CONTENT,
            controller.updateSuggestionStatus("id", Map.of("status", "Seen"))
                .getStatusCode()
        );
        assertEquals("Seen", suggestion.getStatus());
        verify(suggestionRepository).save(suggestion);
    }
}
