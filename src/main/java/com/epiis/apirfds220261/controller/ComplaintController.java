package com.epiis.apirfds220261.controller;

import java.io.IOException;

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

import com.epiis.apirfds220261.business.BusinessComplaint;
import com.epiis.apirfds220261.dto.request.RequestComplaintInsert;
import com.epiis.apirfds220261.dto.response.ResponseComplaintGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseComplaintInsert;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "complaint")
public class ComplaintController {

    private final BusinessComplaint businessComplaint;

    public ComplaintController(BusinessComplaint businessComplaint) {
        this.businessComplaint = businessComplaint;
    }

    @PostMapping(
        path = "insert",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseComplaintInsert> actionInsert(
        @Valid @ModelAttribute RequestComplaintInsert request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ResponseComplaintInsert response = new ResponseComplaintInsert();

            bindingResult.getAllErrors().forEach(
                error -> response.listMessage.add(error.getDefaultMessage())
            );

            return ResponseEntity.badRequest().body(response);
        }

        try {
            return ResponseEntity.ok(businessComplaint.insert(request));
        } catch (IOException _) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

    @GetMapping(path = "getbycode/{code}")
    public ResponseEntity<ResponseComplaintGetByCode> actionGetByCode(
        @PathVariable String code
    ) {
        return ResponseEntity.ok(businessComplaint.getByCode(code));
    }
}