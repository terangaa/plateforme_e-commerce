package com.vente_en_ligne.plateforme_e_commerce.repository;

import com.vente_en_ligne.plateforme_e_commerce.entity.ElementCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des éléments de commande
 */
@Repository
public interface ElementCommandeRepository extends JpaRepository<ElementCommande, Long> {
    List<ElementCommande> findByCommandeId(Long commandeId);
}
