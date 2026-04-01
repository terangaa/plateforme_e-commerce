package com.vente_en_ligne.plateforme_e_commerce.dto;

import lombok.Data;

@Data
public class CategorieResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
}