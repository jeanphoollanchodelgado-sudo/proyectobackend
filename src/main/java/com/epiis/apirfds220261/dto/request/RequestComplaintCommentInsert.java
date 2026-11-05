package com.epiis.apirfds220261.dto.request;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestComplaintCommentInsert {
	@NotBlank(message = "El campo \"code\" es requerido.")
	private String code;
	
	private String idUser;
	
	@NotBlank(message = "El campo \"description\" es requerido.")
	@Size(max = 2000, message = "El campo \"description\" admite hasta 2000 caracteres.")
	private String description;
}