package com.epiis.apirfds220261.dto.response;


import java.time.Instant;
import com.epiis.apirfds220261.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSuggestionGetByCode extends ResponseGeneric {
	public String status;
	
	public String description;
    public String officeName;
    private Instant createdAt;
}
