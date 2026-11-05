package com.epiis.apirfds220261.dto.request;

import java.util.List;


import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSuggestionInsert {
	@NotBlank(message = "El campo \"idOffice\" es requerido.")
	private String idOffice;
	
	private String personFullName;
	
	@NotBlank(message = "El campo \"description\" es requerido.")
	private String description;
	
	private List<MultipartFile> files;
}