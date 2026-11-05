package com.epiis.apirfds220261.dto.response;

import java.time.Instant;

import com.epiis.apirfds220261.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class ResponseComplaintGetByCode extends ResponseGeneric {
	 public String status;
	    public String idComplaint;
	    public String description;
	    public String officeName;
	    public String professorName;
	    public Instant createdAt;
	    public List<ResponseComplaintComment> comments = new ArrayList<>();
}
