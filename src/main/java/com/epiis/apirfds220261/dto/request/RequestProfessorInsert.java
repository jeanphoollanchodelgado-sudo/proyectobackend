package com.epiis.apirfds220261.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestProfessorInsert {
	@NotBlank(message = "El nombre es obligatorio")
	@Size(min=2 , max=80 , message="El nombre debe tener entre 2 y 80 caracteres")
	private String firstName;
	
	@NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 80, message = "El apellido debe tener entre 2 y 80 caracteres")
    private String surName;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo válido")
    private String email;

}
