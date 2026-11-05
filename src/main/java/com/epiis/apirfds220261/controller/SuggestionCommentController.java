package com.epiis.apirfds220261.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.apirfds220261.business.BusinessComments;
import com.epiis.apirfds220261.dto.request.RequestSuggestionCommentInsert;
import com.epiis.apirfds220261.dto.response.ResponseSuggestionCommentGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseSuggestionCommentInsert;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "suggestioncomment")
public class SuggestionCommentController {

    private final BusinessComments businessComments;

    public SuggestionCommentController(
        BusinessComments businessComments
    ) {
        this.businessComments = businessComments;
    }

    @PostMapping(
        path = "insert",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseSuggestionCommentInsert> actionInsert(
        @Valid @ModelAttribute RequestSuggestionCommentInsert request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ResponseSuggestionCommentInsert response =
                new ResponseSuggestionCommentInsert();

            bindingResult.getAllErrors().forEach(
                error -> response.listMessage.add(error.getDefaultMessage())
            );

            return ResponseEntity.badRequest().body(response);
        }

        try {
            return ResponseEntity.ok(
                businessComments.insertSuggestionComment(request)
            );
        } catch (RuntimeException _) {
            ResponseSuggestionCommentInsert response =
                new ResponseSuggestionCommentInsert();

            response.exception();
            response.listMessage.add(
                "Ocurrió un error al registrar el comentario."
            );

            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
        }
    }

    @GetMapping(path = "getbycode/{code}")
    public ResponseEntity<ResponseSuggestionCommentGetByCode> actionGetByCode(
        @PathVariable String code
    ) {
        return ResponseEntity.ok(
            businessComments.getSuggestionComments(code)
        );
    }
}