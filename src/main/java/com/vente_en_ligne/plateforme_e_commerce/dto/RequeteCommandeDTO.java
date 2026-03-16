package com.vente_en_ligne.plateforme_e_commerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * DTO pour recevoir les données d'une commande
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequeteCommandeDTO {
    
    @NotBlank(message = "Le nom du client est requis")
    private String nomClient;
    
    @NotBlank(message = "L'email du client est requis")
    @Email(message = "L'email doit être valide")
    private String emailClient;
    
    @NotBlank(message = "Le téléphone du client est requis")
    private String telephoneClient;
    
    @NotBlank(message = "L'adresse du client est requise")
    private String adresseClient;
    
    @NotEmpty(message = "La commande doit contenir au moins un produit")
    @Valid
    private List<ElementCommandeDTO> elements;
}
