package com.vente_en_ligne.plateforme_e_commerce.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entité représentant une catégorie de produits
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    private String provider;
    private String transactionId;
    private BigDecimal amount;
    private String status;
    private String paymentMethod; // WAVE, ORANGE_MONEY, FREE_MONEY, CASH_ON_DELIVERY
    private java.time.Instant paymentDate;

    @ManyToOne
    private Commande commande;
}