package com.vente_en_ligne.plateforme_e_commerce.controller;

import com.vente_en_ligne.plateforme_e_commerce.dto.ClientDTO;
import com.vente_en_ligne.plateforme_e_commerce.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des clients
 */
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    
    private final ClientService clientService;
    
    @GetMapping
    public ResponseEntity<List<ClientDTO>> obtenirTousLesClients() {
        return ResponseEntity.ok(clientService.obtenirTousLesClients());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> obtenirClientParId(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.obtenirClientParId(id));
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<ClientDTO> obtenirClientParEmail(@PathVariable String email) {
        return ResponseEntity.ok(clientService.obtenirClientParEmail(email));
    }
    
    @PostMapping
    public ResponseEntity<ClientDTO> creerClient(@Valid @RequestBody ClientDTO clientDTO) {
        ClientDTO nouveauClient = clientService.creerClient(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauClient);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> mettreAJourClient(
            @PathVariable Long id, 
            @Valid @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.mettreAJourClient(id, clientDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerClient(@PathVariable Long id) {
        clientService.supprimerClient(id);
        return ResponseEntity.noContent().build();
    }
}
