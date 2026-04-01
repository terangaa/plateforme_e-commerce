package com.vente_en_ligne.plateforme_e_commerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProtectedController {

    @GetMapping("/api/protected/data")
    public Map<String, Object> getProtectedData() {
        return Map.of(
                "message", "Accès autorisé 🔐",
                "status", "success"
        );
    }
}