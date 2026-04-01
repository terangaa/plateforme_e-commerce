package com.vente_en_ligne.plateforme_e_commerce.entity;

import lombok.Getter;

@Getter

public enum StatutPaiement {
    PAYEE("Payée"),
    NON_PAYEE("Non payée");

    private final String label;

    StatutPaiement(String label) {
        this.label = label;
    }

}