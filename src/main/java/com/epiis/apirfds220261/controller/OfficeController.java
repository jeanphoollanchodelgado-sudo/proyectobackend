package com.epiis.apirfds220261.controller;

import org.springframework.http.ResponseEntity;




import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.apirfds220261.business.BusinessOffice;
import com.epiis.apirfds220261.dto.response.ResponseOfficeGetAll;
import com.epiis.apirfds220261.dto.request.RequestOfficeInsert;

import jakarta.validation.Valid;
@RestController
@RequestMapping(path = "admin")
public class OfficeController {

    private final BusinessOffice businessOffice;

    public OfficeController(BusinessOffice businessOffice) {
        this.businessOffice = businessOffice;
    }

    @GetMapping(path = "office/getall")
    public ResponseEntity<ResponseOfficeGetAll> actionGetAll() {
        return ResponseEntity.ok(businessOffice.getAll());
    }

    @PostMapping(path = "office/insert")
    public ResponseEntity<ResponseOfficeGetAll> actionInsert(@Valid @RequestBody RequestOfficeInsert request) {
        return ResponseEntity.ok(businessOffice.insert(request));
    }
}