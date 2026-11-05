package com.epiis.apirfds220261.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUserLogin {
    private String idUser;
    private String firstName;
    private String surName;
    private String email;
    private String role;
    private String token;
}