package com.epiis.app.apirfds220262;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "index")
public class IndexController {

    @GetMapping(path = "index")
    public ResponseEntity<Map<String, String>> actionIndex() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Hola mundo");

        return ResponseEntity.ok(response);
    }
}