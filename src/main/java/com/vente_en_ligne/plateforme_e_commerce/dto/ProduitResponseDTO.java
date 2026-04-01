package com.vente_en_ligne.plateforme_e_commerce.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProduitResponseDTO {
    private Long id;
    private String nom;
    private String description;
    private BigDecimal prix;
    private Integer stock;
    private BigDecimal quantite;
    private String imageUrl;
    private Boolean actif;
    private LocalDateTime createdAt;
    private CategorieResponseDTO categorie;
    private UserResponseDTO proprietaire;
}