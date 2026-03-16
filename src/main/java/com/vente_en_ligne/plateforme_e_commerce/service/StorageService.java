package com.vente_en_ligne.plateforme_e_commerce.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    private final Path uploadDir = Paths.get("src/main/resources/static/images/valises"); // dossier relatif

    public StorageService() {
        try {
            Files.createDirectories(uploadDir); // crée le dossier s'il n'existe pas
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier de stockage.", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Nom de fichier invalide : " + fileName);
            }

            Path targetLocation = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/images/valises/" + fileName; // URL pour Thymeleaf
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }
}
