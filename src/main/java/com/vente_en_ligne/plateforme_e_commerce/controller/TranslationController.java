package com.vente_en_ligne.plateforme_e_commerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vente_en_ligne.plateforme_e_commerce.service.TranslationService;

import lombok.RequiredArgsConstructor;

/**
 * Controller REST pour les traductions et le changement de langue
 */
@RestController
@RequestMapping("/api/translation")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    /**
     * Obtenir toutes les langues supportées
     */
    @GetMapping("/languages")
    public ResponseEntity<List<Map<String, String>>> getSupportedLanguages() {
        Map<String, String> languages = TranslationService.getSupportedLanguages();

        List<Map<String, String>> result = languages.entrySet().stream()
                .map(entry -> {
                    Map<String, String> lang = new HashMap<>();
                    lang.put("code", entry.getKey());
                    lang.put("name", entry.getValue());
                    return lang;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Obtenir la langue actuelle
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, String>> getCurrentLanguage() {
        Map<String, String> response = new HashMap<>();
        response.put("language", translationService.getCurrentLanguage());

        Map<String, String> languages = TranslationService.getSupportedLanguages();
        response.put("languageName", languages.get(translationService.getCurrentLanguage()));

        return ResponseEntity.ok(response);
    }

    /**
     * Changer la langue
     */
    @PostMapping("/change")
    public ResponseEntity<Map<String, Object>> changeLanguage(@RequestBody Map<String, String> request) {
        String languageCode = request.get("language");

        if (languageCode == null || languageCode.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Code langue requis"
            ));
        }

        translationService.setLanguage(languageCode);

        Map<String, String> languages = TranslationService.getSupportedLanguages();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "language", languageCode,
                "languageName", languages.get(languageCode)
        ));
    }

    /**
     * Obtenir toutes les traductions pour la langue actuelle
     */
    @GetMapping("/translations")
    public ResponseEntity<Map<String, String>> getAllTranslations() {
        return ResponseEntity.ok(translationService.getAllTranslations());
    }

    /**
     * Obtenir toutes les traductions pour une langue spécifique
     */
    @GetMapping("/translations/{languageCode}")
    public ResponseEntity<Map<String, String>> getTranslationsByLanguage(
            @PathVariable String languageCode) {

        Map<String, String> languages = TranslationService.getSupportedLanguages();
        if (!languages.containsKey(languageCode)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Langue non supportée"
            ));
        }

        return ResponseEntity.ok(translationService.getAllTranslations(languageCode));
    }

    /**
     * Traduire une clé spécifique
     */
    @GetMapping("/translate")
    public ResponseEntity<Map<String, String>> translate(
            @RequestParam String key,
            @RequestParam(required = false, defaultValue = "fr") String language) {

        String translation = translationService.translate(key, language);

        Map<String, String> response = new HashMap<>();
        response.put("key", key);
        response.put("language", language);
        response.put("translation", translation);

        return ResponseEntity.ok(response);
    }
}
