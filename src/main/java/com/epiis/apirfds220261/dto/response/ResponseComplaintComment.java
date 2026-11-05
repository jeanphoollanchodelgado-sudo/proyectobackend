package com.epiis.apirfds220261.dto.response;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseComplaintComment {
    private String id;
    private String author;
    private String description;
    private Date createdAt;
}