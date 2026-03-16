package com.vente_en_ligne.plateforme_e_commerce.repository;

import com.vente_en_ligne.plateforme_e_commerce.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour la gestion des produits
 */
@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByCategorieId(Long categorieId);
    List<Produit> findByActifTrue();
    List<Produit> findByCategorieIdAndActifTrue(Long categorieId);

    // Les 10 derniers produits par date de création
    List<Produit> findTop10ByOrderByCreatedAtDesc();

    // On prend seulement le dernier
   // Produit findTopByOrderByCreatedAtDesc();
}
