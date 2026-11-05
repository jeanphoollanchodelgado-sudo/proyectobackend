package com.epiis.apirfds220261.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.apirfds220261.business.BusinessUser;
import com.epiis.apirfds220261.dto.request.RequestUserRegister;
import com.epiis.apirfds220261.dto.response.ResponseUserRegister;

import com.epiis.apirfds220261.dto.request.RequestUserLogin;
import com.epiis.apirfds220261.dto.response.ResponseUserLogin;


import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "user")
public class UserController {
    private final BusinessUser businessUser;

    public UserController(BusinessUser businessUser) {
        this.businessUser = businessUser;
    }

    @PostMapping(path = "register")
    public ResponseEntity<ResponseUserRegister> register(
            @Valid @RequestBody RequestUserRegister request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ResponseUserRegister response = new ResponseUserRegister();
            bindingResult.getAllErrors().forEach(error ->
                response.listMessage.add(error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(businessUser.register(request));
    }
    
    @PostMapping(path = "login")
    public ResponseEntity<ResponseUserLogin> login(
            @Valid @RequestBody RequestUserLogin request) {
        return ResponseEntity.ok(businessUser.login(request));
    }
}