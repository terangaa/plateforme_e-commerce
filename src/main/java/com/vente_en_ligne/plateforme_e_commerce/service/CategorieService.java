package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.dto.CategorieDTO;
import com.vente_en_ligne.plateforme_e_commerce.entity.Categorie;
import com.vente_en_ligne.plateforme_e_commerce.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des catégories
 */
@Service
@RequiredArgsConstructor
public class CategorieService {
    
    private final CategorieRepository categorieRepository;
    
    public List<CategorieDTO> obtenirToutesLesCategories() {
        return categorieRepository.findAll().stream()
                .map(this::versDTO)
                .collect(Collectors.toList());
    }
    
    public CategorieDTO obtenirCategorieParId(Long id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));
        return versDTO(categorie);
    }
    
    @Transactional
    public CategorieDTO creerCategorie(CategorieDTO categorieDTO) {
        if (categorieRepository.existsByName(categorieDTO.getNom())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }
        Categorie categorie = versEntite(categorieDTO);
        categorie = categorieRepository.save(categorie);
        return versDTO(categorie);
    }
    
    @Transactional
    public CategorieDTO mettreAJourCategorie(Long id, CategorieDTO categorieDTO) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));
        
        categorie.setName(categorieDTO.getNom());
        categorie.setDescription(categorieDTO.getDescription());
        
        categorie = categorieRepository.save(categorie);
        return versDTO(categorie);
    }
    
    @Transactional
    public void supprimerCategorie(Long id) {
        if (!categorieRepository.existsById(id)) {
            throw new RuntimeException("Catégorie non trouvée avec l'ID: " + id);
        }
        categorieRepository.deleteById(id);
    }
    
    private CategorieDTO versDTO(Categorie categorie) {
        CategorieDTO dto = new CategorieDTO();
        dto.setId(categorie.getId());
        dto.setNom(categorie.getName());
        dto.setDescription(categorie.getDescription());
        return dto;
    }
    
    private Categorie versEntite(CategorieDTO dto) {
        Categorie categorie = new Categorie();
        categorie.setName(dto.getNom());
        categorie.setDescription(dto.getDescription());
        return categorie;
    }

    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll(); // récupère toutes les catégories depuis la base
    }

    public Categorie findById(Long categorieId) {
        return categorieRepository.findById(categorieId)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable avec l'ID " + categorieId));
    }
}
