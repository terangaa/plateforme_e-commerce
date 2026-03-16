package com.vente_en_ligne.plateforme_e_commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO pour les éléments du panier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long produitId;
    private String nom;
    private BigDecimal prix;
    private String imageUrl;
    private Integer quantite;
    
    public CartItemDTO(Long produitId, String nom, BigDecimal prix, String imageUrl) {
        this.produitId = produitId;
        this.nom = nom;
        this.prix = prix;
        this.imageUrl = imageUrl;
        this.quantite = 1;
    }
}
