package com.epiis.apirfds220261.dto.response;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epiis.apirfds220261.generic.ResponseGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSuggestionCommentGetByCode extends ResponseGeneric {
	List<Map<String, String>> listSuggestionComment = new ArrayList<>();
}