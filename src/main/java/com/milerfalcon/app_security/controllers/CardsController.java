package com.milerfalcon.app_security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("cards")
public class CardsController {

    @GetMapping
    public Map<String, String> cards() {
        return Collections.singletonMap("message", "Hola desde cards");
    }
}
