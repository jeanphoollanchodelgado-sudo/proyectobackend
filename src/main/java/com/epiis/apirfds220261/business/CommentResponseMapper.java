package com.epiis.apirfds220261.business;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.epiis.apirfds220261.entity.EntityUser;

final class CommentResponseMapper {

    private static final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private CommentResponseMapper() {
    }

    static Map<String, String> toMap(
        String idKey,
        String id,
        String code,
        String idUser,
        EntityUser user,
        String description,
        Instant createdAt
    ) {
        Map<String, String> data = new HashMap<>();
        data.put(idKey, id);
        data.put("code", code);
        data.put("idUser", idUser == null ? "" : idUser);
        data.put(
            "personFullName",
            user == null ? "" : user.getFirstName() + " " + user.getSurName()
        );
        data.put("description", description);
        data.put("createdAt", DATE_FORMAT.format(createdAt));
        return data;
    }
}
