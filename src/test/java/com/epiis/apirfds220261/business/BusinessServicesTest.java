package com.epiis.apirfds220261.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.epiis.apirfds220261.controller.JwtService;
import com.epiis.apirfds220261.dto.request.RequestAdminFilter;
import com.epiis.apirfds220261.dto.request.RequestComplaintInsert;
import com.epiis.apirfds220261.dto.request.RequestOfficeInsert;
import com.epiis.apirfds220261.dto.request.RequestProfessorInsert;
import com.epiis.apirfds220261.dto.request.RequestSuggestionInsert;
import com.epiis.apirfds220261.dto.request.RequestUserLogin;
import com.epiis.apirfds220261.dto.request.RequestUserRegister;
import com.epiis.apirfds220261.entity.EntityComplaint;
import com.epiis.apirfds220261.entity.EntityOffice;
import com.epiis.apirfds220261.entity.EntityProfessor;
import com.epiis.apirfds220261.entity.EntitySuggestion;
import com.epiis.apirfds220261.entity.EntityUser;
import com.epiis.apirfds220261.repository.RepositoryComplaint;
import com.epiis.apirfds220261.repository.RepositoryComplaintFile;
import com.epiis.apirfds220261.repository.RepositoryOffice;
import com.epiis.apirfds220261.repository.RepositoryProfessor;
import com.epiis.apirfds220261.repository.RepositorySuggestion;
import com.epiis.apirfds220261.repository.RepositorySuggestionFile;
import com.epiis.apirfds220261.repository.RepositoryUser;

@ExtendWith(MockitoExtension.class)
class BusinessServicesTest {

    @Mock
    private RepositoryComplaint complaintRepository;
    @Mock
    private RepositoryComplaintFile complaintFileRepository;
    @Mock
    private RepositorySuggestion suggestionRepository;
    @Mock
    private RepositorySuggestionFile suggestionFileRepository;
    @Mock
    private RepositoryOffice officeRepository;
    @Mock
    private RepositoryProfessor professorRepository;
    @Mock
    private RepositoryUser userRepository;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private BusinessComplaint businessComplaint;
    @InjectMocks
    private BusinessSuggestion businessSuggestion;
    @InjectMocks
    private BusinessOffice businessOffice;
    @InjectMocks
    private BusinessProfessor businessProfessor;
    @InjectMocks
    private BusinessUser businessUser;
    @InjectMocks
    private BusinessAdmin businessAdmin;

    @Test
    void insertsAndReadsComplaint() throws Exception {
        RequestComplaintInsert request = new RequestComplaintInsert();
        request.setIdOffice("office");
        request.setIdProfessor("professor");
        request.setComplaintFullName("  report  ");
        request.setPersonFullName("  person  ");
        request.setDescription("  details  ");
        request.setIssueDate(LocalDate.of(2026, 1, 2));
        request.setFiles(null);

        var inserted = businessComplaint.insert(request);
        assertEquals("success", inserted.getType());
        assertEquals(7, inserted.getCode().length());
        verify(complaintRepository).save(any(EntityComplaint.class));
        verify(complaintFileRepository, never()).saveAll(anyList());

        EntityComplaint found = new EntityComplaint();
        found.setIdComplaint("id");
        found.setStatus("Pendiente");
        found.setDescription("details");
        found.setCreatedAt(java.time.Instant.now());
        found.setParentOffice(new EntityOffice());
        found.getParentOffice().setName("Office");
        found.setParentProfessor(new EntityProfessor());
        found.getParentProfessor().setFirstName("Ada");
        found.getParentProfessor().setSurName("Lovelace");
        when(complaintRepository.findByCode("code")).thenReturn(found);

        var read = businessComplaint.getByCode("code");
        assertEquals("success", read.getType());
        assertEquals("Office", read.getOfficeName());
        assertEquals("Ada Lovelace", read.getProfessorName());

        when(complaintRepository.findByCode("missing")).thenReturn(null);
        assertEquals("error", businessComplaint.getByCode("missing").getType());
    }

    @Test
    void insertsAndReadsSuggestion() throws Exception {
        RequestSuggestionInsert request = new RequestSuggestionInsert();
        request.setIdOffice("office");
        request.setPersonFullName("  person  ");
        request.setDescription("  idea  ");
        request.setFiles(null);

        var inserted = businessSuggestion.insert(request);
        assertEquals("success", inserted.getType());
        assertEquals(7, inserted.getCode().length());
        verify(suggestionRepository).save(any(EntitySuggestion.class));
        verify(suggestionFileRepository, never()).saveAll(anyList());

        EntitySuggestion found = new EntitySuggestion();
        found.setIdSuggestion("id");
        found.setStatus("Pendiente");
        found.setDescription("idea");
        found.setCreatedAt(java.time.Instant.now());
        found.setParentOffice(new EntityOffice());
        found.getParentOffice().setName("Office");
        when(suggestionRepository.findByCode("code")).thenReturn(found);

        var read = businessSuggestion.getByCode("code");
        assertEquals("success", read.getType());
        assertEquals("Office", read.getOfficeName());

        when(suggestionRepository.findByCode("missing")).thenReturn(null);
        assertEquals("error", businessSuggestion.getByCode("missing").getType());
    }

    @Test
    void adminNormalizesEmptyAndAllFilters() {
        RequestAdminFilter filter = new RequestAdminFilter();
        filter.setIdOffice("  ");
        filter.setStatus("tOdOs");
        when(complaintRepository.findByFilters(null, null)).thenReturn(List.of());
        when(suggestionRepository.findByFilters(null, null)).thenReturn(List.of());
        assertEquals(List.of(), businessAdmin.getComplaints(filter));
        assertEquals(List.of(), businessAdmin.getSuggestions(filter));

        filter.setIdOffice(" office ");
        filter.setStatus(" Pendiente ");
        businessAdmin.getComplaints(filter);
        businessAdmin.getSuggestions(filter);
        verify(complaintRepository).findByFilters("office", "Pendiente");
        verify(suggestionRepository).findByFilters("office", "Pendiente");
    }

    @Test
    void officeValidatesDuplicatesAndCreatesNewRecords() {
        RequestOfficeInsert request = new RequestOfficeInsert();
        request.setName("  ");
        assertEquals("error", businessOffice.insert(request).getType());
        verify(officeRepository, never()).save(any(EntityOffice.class));

        request.setName(" Main ");
        when(officeRepository.findAll()).thenReturn(List.of());
        assertEquals("success", businessOffice.insert(request).getType());
        verify(officeRepository).save(any(EntityOffice.class));

        when(officeRepository.findAll()).thenReturn(List.of(office("MAIN")));
        assertEquals("warning", businessOffice.insert(request).getType());
        assertEquals(1, businessOffice.getAll().getListOffice().size());
    }

    @Test
    void professorValidatesDuplicatesAndCreatesNewRecords() {
        RequestProfessorInsert request = new RequestProfessorInsert();
        request.setFirstName(" ");
        request.setSurName("Name");
        request.setEmail("mail@example.com");
        assertEquals("error", businessProfessor.insert(request).getType());

        request.setFirstName(" Ada ");
        request.setSurName(" Lovelace ");
        when(professorRepository.findAll()).thenReturn(List.of());
        assertEquals("success", businessProfessor.insert(request).getType());
        verify(professorRepository).save(any(EntityProfessor.class));

        when(professorRepository.findAll()).thenReturn(List.of(professor("ada", "LOVELACE")));
        assertEquals("warning", businessProfessor.insert(request).getType());
        assertEquals(1, businessProfessor.getAll().getListProfessor().size());
    }

    @Test
    void registersAndAuthenticatesUsers() {
        RequestUserRegister registration = new RequestUserRegister();
        registration.setFirstName(" Ada ");
        registration.setSurName(" Lovelace ");
        registration.setEmail(" ADA@example.com ");
        registration.setPassword("secret");
        when(userRepository.existsByEmailIgnoreCase("ADA@example.com")).thenReturn(false);
        var registered = businessUser.register(registration);
        assertEquals("success", registered.getType());
        assertNotNull(registered.getIdUser());
        verify(userRepository).save(any(EntityUser.class));

        when(userRepository.existsByEmailIgnoreCase("ADA@example.com")).thenReturn(true);
        assertEquals("warning", businessUser.register(registration).getType());

        RequestUserLogin login = new RequestUserLogin();
        login.setEmail(" ada@example.com ");
        login.setPassword("secret");
        when(userRepository.findByEmailIgnoreCase("ada@example.com"))
            .thenReturn(Optional.empty());
        ResponseStatusException absent = assertThrows(
            ResponseStatusException.class,
            () -> businessUser.login(login)
        );
        assertEquals(HttpStatus.UNAUTHORIZED, absent.getStatusCode());

        EntityUser user = new EntityUser();
        user.setIdUser("id");
        user.setFirstName("Ada");
        user.setSurName("Lovelace");
        user.setEmail("ada@example.com");
        user.setRole("Estudiante");
        user.setPassword(new BCryptPasswordEncoder().encode("secret"));
        when(userRepository.findByEmailIgnoreCase("ada@example.com"))
            .thenReturn(Optional.of(user));

        login.setPassword("wrong");
        assertThrows(ResponseStatusException.class, () -> businessUser.login(login));

        login.setPassword("secret");
        when(jwtService.generateToken(user)).thenReturn("jwt");
        assertEquals("jwt", businessUser.login(login).getToken());
    }

    private static EntityOffice office(String name) {
        EntityOffice office = new EntityOffice();
        office.setIdOffice("office");
        office.setName(name);
        return office;
    }

    private static EntityProfessor professor(String firstName, String surName) {
        EntityProfessor professor = new EntityProfessor();
        professor.setFirstName(firstName);
        professor.setSurName(surName);
        return professor;
    }
}
