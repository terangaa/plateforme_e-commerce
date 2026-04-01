package com.vente_en_ligne.plateforme_e_commerce.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String nomComplet;
    private String email;
}