package com.vente_en_ligne.plateforme_e_commerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour l'API REST
 */
@RestControllerAdvice
public class GestionnaireExceptionGlobal {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> gererExceptionsValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> reponse = new HashMap<>();
        Map<String, String> erreurs = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(erreur -> {
            String nomChamp = ((FieldError) erreur).getField();
            String messageErreur = erreur.getDefaultMessage();
            erreurs.put(nomChamp, messageErreur);
        });
        
        reponse.put("statut", "erreur");
        reponse.put("message", "Échec de la validation");
        reponse.put("erreurs", erreurs);
        
        return new ResponseEntity<>(reponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> gererExceptionRuntime(RuntimeException ex) {
        Map<String, String> reponse = new HashMap<>();
        reponse.put("statut", "erreur");
        reponse.put("message", ex.getMessage());
        return new ResponseEntity<>(reponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> gererExceptionGenerique(Exception ex) {
        Map<String, String> reponse = new HashMap<>();
        reponse.put("statut", "erreur");
        reponse.put("message", "Une erreur inattendue s'est produite: " + ex.getMessage());
        return new ResponseEntity<>(reponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
