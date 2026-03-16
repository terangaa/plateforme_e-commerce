package com.vente_en_ligne.plateforme_e_commerce.repository;

import com.vente_en_ligne.plateforme_e_commerce.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository pour la gestion des catégories
 */
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    Optional<Categorie> findByName(String name);
    boolean existsByName(String name);
}
