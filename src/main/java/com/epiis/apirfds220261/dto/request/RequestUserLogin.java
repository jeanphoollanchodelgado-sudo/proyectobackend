package com.epiis.apirfds220261.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUserLogin {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
    private String token;
}