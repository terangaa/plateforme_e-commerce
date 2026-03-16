package com.vente_en_ligne.plateforme_e_commerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO pour la transférer les données d'un produit
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitDTO {
    private Long id;
    
    @NotBlank(message = "Le nom du produit est requis")
    private String nom;
    
    private String description;
    
    @NotNull(message = "Le prix est requis")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal prix;
    
    private Integer stock;
    
    private String imageUrl;
    
    private Long categorieId;
    
    private String nomCategorie;
    
    private Boolean actif = true;
}
