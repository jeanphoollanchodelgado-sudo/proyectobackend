package com.epiis.apirfds220261.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUserRegister {
    @NotBlank
    @Size(max = 70)
    private String firstName;

    @NotBlank
    @Size(max = 40)
    private String surName;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}