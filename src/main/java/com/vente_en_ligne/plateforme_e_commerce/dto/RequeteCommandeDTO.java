package com.vente_en_ligne.plateforme_e_commerce.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour recevoir les données d'une commande
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequeteCommandeDTO {

    // Champs envoyés par le frontend (payment.html)
    private Long clientId;

    @NotBlank(message = "Le nom du client est requis")
    private String clientNom;

    private String clientPrenom;

    @NotBlank(message = "L'email du client est requis")
    @Email(message = "L'email doit être valide")
    private String clientEmail;

    @NotBlank(message = "Le téléphone du client est requis")
    private String clientTelephone;

    @NotBlank(message = "L'adresse du client est requise")
    private String adresse;

    private String ville;

    private String codePostal;

    private String modePaiement;

    @NotEmpty(message = "La commande doit contenir au moins un produit")
    @Valid
    private List<ElementCommandeDTO> produits;
}
