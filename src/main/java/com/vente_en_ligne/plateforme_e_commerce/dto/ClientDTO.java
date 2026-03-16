package com.vente_en_ligne.plateforme_e_commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO pour transférer les données d'un client
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    
    @NotBlank(message = "Le nom du client est requis")
    private String nom;
    
    @NotBlank(message = "L'email du client est requis")
    @Email(message = "L'email doit être valide")
    private String email;
    
    @NotBlank(message = "Le téléphone du client est requis")
    private String telephone;
    
    private String adresse;
}
