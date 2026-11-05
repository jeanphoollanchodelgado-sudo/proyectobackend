package com.epiis.apirfds220261.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.epiis.apirfds220261.business.BusinessComplaint;
import com.epiis.apirfds220261.business.BusinessComments;
import com.epiis.apirfds220261.business.BusinessOffice;
import com.epiis.apirfds220261.business.BusinessProfessor;
import com.epiis.apirfds220261.business.BusinessSuggestion;
import com.epiis.apirfds220261.business.BusinessUser;
import com.epiis.apirfds220261.dto.request.RequestComplaintCommentInsert;
import com.epiis.apirfds220261.dto.request.RequestComplaintInsert;
import com.epiis.apirfds220261.dto.request.RequestOfficeInsert;
import com.epiis.apirfds220261.dto.request.RequestProfessorInsert;
import com.epiis.apirfds220261.dto.request.RequestSuggestionCommentInsert;
import com.epiis.apirfds220261.dto.request.RequestSuggestionInsert;
import com.epiis.apirfds220261.dto.request.RequestUserLogin;
import com.epiis.apirfds220261.dto.request.RequestUserRegister;
import com.epiis.apirfds220261.dto.response.ResponseComplaintCommentGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseComplaintCommentInsert;
import com.epiis.apirfds220261.dto.response.ResponseComplaintGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseComplaintInsert;
import com.epiis.apirfds220261.dto.response.ResponseOfficeGetAll;
import com.epiis.apirfds220261.dto.response.ResponseProfessorGetAll;
import com.epiis.apirfds220261.dto.response.ResponseSuggestionCommentGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseSuggestionCommentInsert;
import com.epiis.apirfds220261.dto.response.ResponseSuggestionGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseSuggestionInsert;
import com.epiis.apirfds220261.dto.response.ResponseUserLogin;
import com.epiis.apirfds220261.dto.response.ResponseUserRegister;

@ExtendWith(MockitoExtension.class)
class ControllerMethodsTest {

    @Mock
    private BusinessComplaint complaintBusiness;
    @Mock
    private BusinessSuggestion suggestionBusiness;
    @Mock
    private BusinessComments businessComments;
    @Mock
    private BusinessOffice officeBusiness;
    @Mock
    private BusinessProfessor professorBusiness;
    @Mock
    private BusinessUser userBusiness;

    @InjectMocks
    private ComplaintController complaintController;
    @InjectMocks
    private SuggestionController suggestionController;
    @InjectMocks
    private ComplaintCommentController complaintCommentController;
    @InjectMocks
    private SuggestionCommentController suggestionCommentController;
    @InjectMocks
    private OfficeController officeController;
    @InjectMocks
    private ProfessorController professorController;
    @InjectMocks
    private UserController userController;

    @Test
    void complaintControllerHandlesValidationIoAndReads() throws Exception {
        RequestComplaintInsert request = new RequestComplaintInsert();
        BindingResult errors = errors();
        assertEquals(
            HttpStatus.BAD_REQUEST,
            complaintController.actionInsert(request, errors).getStatusCode()
        );

        when(complaintBusiness.insert(request)).thenReturn(new ResponseComplaintInsert());
        assertEquals(
            HttpStatus.OK,
            complaintController.actionInsert(request, noErrors()).getStatusCode()
        );
        when(complaintBusiness.insert(request)).thenThrow(new IOException());
        assertEquals(
            HttpStatus.INTERNAL_SERVER_ERROR,
            complaintController.actionInsert(request, noErrors()).getStatusCode()
        );

        when(complaintBusiness.getByCode("code")).thenReturn(new ResponseComplaintGetByCode());
        assertEquals(
            HttpStatus.OK,
            complaintController.actionGetByCode("code").getStatusCode()
        );
    }

    @Test
    void suggestionControllerHandlesValidationIoAndReads() throws Exception {
        RequestSuggestionInsert request = new RequestSuggestionInsert();
        assertEquals(
            HttpStatus.BAD_REQUEST,
            suggestionController.actionInsert(request, errors()).getStatusCode()
        );
        when(suggestionBusiness.insert(request)).thenReturn(new ResponseSuggestionInsert());
        assertEquals(
            HttpStatus.OK,
            suggestionController.actionInsert(request, noErrors()).getStatusCode()
        );
        when(suggestionBusiness.insert(request)).thenThrow(new IOException());
        assertEquals(
            HttpStatus.INTERNAL_SERVER_ERROR,
            suggestionController.actionInsert(request, noErrors()).getStatusCode()
        );
        when(suggestionBusiness.getByCode("code")).thenReturn(new ResponseSuggestionGetByCode());
        assertEquals(
            HttpStatus.OK,
            suggestionController.actionGetByCode("code").getStatusCode()
        );
    }

    @Test
    void commentControllersHandleValidationExceptionsAndReads() {
        RequestComplaintCommentInsert complaintRequest = new RequestComplaintCommentInsert();
        assertEquals(
            HttpStatus.BAD_REQUEST,
            complaintCommentController.actionInsert(complaintRequest, errors()).getStatusCode()
        );
        when(businessComments.insertComplaintComment(complaintRequest))
            .thenReturn(new ResponseComplaintCommentInsert());
        assertEquals(
            HttpStatus.OK,
            complaintCommentController.actionInsert(complaintRequest, noErrors()).getStatusCode()
        );
        when(businessComments.insertComplaintComment(complaintRequest))
            .thenThrow(new IllegalStateException("failure"));
        assertEquals(
            HttpStatus.INTERNAL_SERVER_ERROR,
            complaintCommentController.actionInsert(complaintRequest, noErrors()).getStatusCode()
        );
        when(businessComments.getComplaintComments("code"))
            .thenReturn(new ResponseComplaintCommentGetByCode());
        assertEquals(
            HttpStatus.OK,
            complaintCommentController.actionGetByCode("code").getStatusCode()
        );

        RequestSuggestionCommentInsert suggestionRequest = new RequestSuggestionCommentInsert();
        assertEquals(
            HttpStatus.BAD_REQUEST,
            suggestionCommentController.actionInsert(suggestionRequest, errors()).getStatusCode()
        );
        when(businessComments.insertSuggestionComment(suggestionRequest))
            .thenReturn(new ResponseSuggestionCommentInsert());
        assertEquals(
            HttpStatus.OK,
            suggestionCommentController.actionInsert(suggestionRequest, noErrors()).getStatusCode()
        );
        when(businessComments.insertSuggestionComment(suggestionRequest))
            .thenThrow(new IllegalStateException("failure"));
        assertEquals(
            HttpStatus.INTERNAL_SERVER_ERROR,
            suggestionCommentController.actionInsert(suggestionRequest, noErrors()).getStatusCode()
        );
        when(businessComments.getSuggestionComments("code"))
            .thenReturn(new ResponseSuggestionCommentGetByCode());
        assertEquals(
            HttpStatus.OK,
            suggestionCommentController.actionGetByCode("code").getStatusCode()
        );
    }

    @Test
    void officeAndProfessorControllersDelegate() {
        when(officeBusiness.getAll()).thenReturn(new ResponseOfficeGetAll());
        when(officeBusiness.insert(org.mockito.ArgumentMatchers.any()))
            .thenReturn(new ResponseOfficeGetAll());
        assertEquals(HttpStatus.OK, officeController.actionGetAll().getStatusCode());
        assertEquals(
            HttpStatus.OK,
            officeController.actionInsert(new RequestOfficeInsert()).getStatusCode()
        );

        when(professorBusiness.getAll()).thenReturn(new ResponseProfessorGetAll());
        when(professorBusiness.insert(org.mockito.ArgumentMatchers.any()))
            .thenReturn(new ResponseProfessorGetAll());
        assertEquals(HttpStatus.OK, professorController.actionGetAll().getStatusCode());
        assertEquals(
            HttpStatus.OK,
            professorController.actionInsert(new RequestProfessorInsert()).getStatusCode()
        );
    }

    @Test
    void userControllerHandlesValidationAndDelegates() {
        RequestUserRegister registration = new RequestUserRegister();
        assertEquals(
            HttpStatus.BAD_REQUEST,
            userController.register(registration, errors()).getStatusCode()
        );
        when(userBusiness.register(registration)).thenReturn(new ResponseUserRegister());
        assertEquals(
            HttpStatus.OK,
            userController.register(registration, noErrors()).getStatusCode()
        );

        RequestUserLogin login = new RequestUserLogin();
        when(userBusiness.login(login)).thenReturn(new ResponseUserLogin());
        assertEquals(HttpStatus.OK, userController.login(login).getStatusCode());
    }

    @Test
    void indexControllersReturnTheirWelcomeMessages() {
        assertEquals(
            Map.of("welcome", "Hola mundo cruel!!!"),
            new GeneralController().actionIndex().getBody()
        );
        assertEquals(
            Map.of("mensaje", "Hola mundo"),
            new com.epiis.app.apirfds220262.IndexController()
                .actionIndex().getBody()
        );
    }

    private static BindingResult noErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        return result;
    }

    private static BindingResult errors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        when(result.getAllErrors()).thenReturn(
            List.of(new FieldError("request", "field", "invalid"))
        );
        return result;
    }
}
