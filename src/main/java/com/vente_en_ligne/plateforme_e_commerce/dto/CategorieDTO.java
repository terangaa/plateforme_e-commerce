package com.vente_en_ligne.plateforme_e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO pour transférer les données d'une catégorie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieDTO {
    private Long id;
    
    @NotBlank(message = "Le nom de la catégorie est requis")
    private String nom;
    
    private String description;
}
