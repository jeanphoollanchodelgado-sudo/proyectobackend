package com.epiis.apirfds220261.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestOfficeInsert {
	@NotBlank(message = "El nombre de la oficina es obligatorio")
	@Size(min =2 , max=100 , message ="El nombre debe tener entre 2 y 100 caracteres")
	private String name;

}
