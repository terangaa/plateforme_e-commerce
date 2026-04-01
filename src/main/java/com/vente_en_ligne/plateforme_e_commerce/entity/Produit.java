package com.vente_en_ligne.plateforme_e_commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String nom;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix = BigDecimal.ZERO;
    private BigDecimal ancienPrix;

    private Integer stock = 0;

    private BigDecimal quantite = BigDecimal.ZERO;

    private String imageUrl;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
    // Champ promotion
    private Boolean promotion;

    // Relation avec Categorie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categorie categorie;

    // Date de création
    private LocalDateTime createdAt;

    // Relation obligatoire avec User (propriétaire)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private User proprietaire;

    // Avant insertion en base
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (active == null) {
            active = true;
        }
        if (prix == null) {
            prix = BigDecimal.ZERO;
        }
        if (stock == null) {
            stock = 0;
        }
        if (quantite == null) {
            quantite = BigDecimal.ZERO;
        }
        // 🔥 Proprietaire NE DOIT PAS être défini ici → gérer via le service
    }
}