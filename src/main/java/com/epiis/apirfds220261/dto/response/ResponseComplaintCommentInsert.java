package com.epiis.apirfds220261.dto.response;


import com.epiis.apirfds220261.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseComplaintCommentInsert extends ResponseGeneric {
	public String idComplaintcomment;
}