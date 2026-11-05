package com.epiis.apirfds220261.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.apirfds220261.business.BusinessProfessor;
import com.epiis.apirfds220261.dto.response.ResponseProfessorGetAll;
import com.epiis.apirfds220261.dto.request.RequestProfessorInsert;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "admin")
public class ProfessorController {

    private final BusinessProfessor businessProfessor;

    public ProfessorController(BusinessProfessor businessProfessor) {
        this.businessProfessor = businessProfessor;
    }

    @GetMapping(path = "professor/getall")
    public ResponseEntity<ResponseProfessorGetAll> actionGetAll() {
        return ResponseEntity.ok(businessProfessor.getAll());
    }

    @PostMapping(path = "professor/insert")
    public ResponseEntity<ResponseProfessorGetAll> actionInsert(@Valid @RequestBody RequestProfessorInsert request) {
        return ResponseEntity.ok(businessProfessor.insert(request));
    }
}