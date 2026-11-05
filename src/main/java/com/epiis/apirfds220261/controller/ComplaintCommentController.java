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
import com.epiis.apirfds220261.dto.request.RequestComplaintCommentInsert;
import com.epiis.apirfds220261.dto.response.ResponseComplaintCommentGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseComplaintCommentInsert;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "complaintcomment")
public class ComplaintCommentController {

    private final BusinessComments businessComments;

    public ComplaintCommentController(
        BusinessComments businessComments
    ) {
        this.businessComments = businessComments;
    }

    @PostMapping(
        path = "insert",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseComplaintCommentInsert> actionInsert(
        @Valid @ModelAttribute RequestComplaintCommentInsert request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ResponseComplaintCommentInsert response =
                new ResponseComplaintCommentInsert();

            bindingResult.getAllErrors().forEach(
                error -> response.listMessage.add(error.getDefaultMessage())
            );

            return ResponseEntity.badRequest().body(response);
        }

        try {
            return ResponseEntity.ok(
                businessComments.insertComplaintComment(request)
            );
        } catch (RuntimeException _) {
            ResponseComplaintCommentInsert response =
                new ResponseComplaintCommentInsert();

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
    public ResponseEntity<ResponseComplaintCommentGetByCode> actionGetByCode(
        @PathVariable String code
    ) {
        return ResponseEntity.ok(
            businessComments.getComplaintComments(code)
        );
    }
}