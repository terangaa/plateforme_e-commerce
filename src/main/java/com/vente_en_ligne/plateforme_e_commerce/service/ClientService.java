package com.vente_en_ligne.plateforme_e_commerce.service;

import com.vente_en_ligne.plateforme_e_commerce.dto.ClientDTO;
import com.vente_en_ligne.plateforme_e_commerce.entity.Client;
import com.vente_en_ligne.plateforme_e_commerce.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des clients
 */
@Service
@RequiredArgsConstructor
public class ClientService {
    
    private final ClientRepository clientRepository;
    
    public List<ClientDTO> obtenirTousLesClients() {
        return clientRepository.findAll().stream()
                .map(this::versDTO)
                .collect(Collectors.toList());
    }
    
    public ClientDTO obtenirClientParId(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + id));
        return versDTO(client);
    }
    
    public ClientDTO obtenirClientParEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'email: " + email));
        return versDTO(client);
    }
    
    @Transactional
    public ClientDTO creerClient(ClientDTO clientDTO) {
        if (clientRepository.existsByEmail(clientDTO.getEmail())) {
            throw new RuntimeException("Un client avec cet email existe déjà");
        }
        Client client = versEntite(clientDTO);
        client = clientRepository.save(client);
        return versDTO(client);
    }
    
    @Transactional
    public ClientDTO mettreAJourClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + id));
        
        client.setNom(clientDTO.getNom());
        client.setEmail(clientDTO.getEmail());
        client.setTelephone(clientDTO.getTelephone());
        client.setAdresse(clientDTO.getAdresse());
        
        client = clientRepository.save(client);
        return versDTO(client);
    }
    
    @Transactional
    public void supprimerClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client non trouvé avec l'ID: " + id);
        }
        clientRepository.deleteById(id);
    }
    
    private ClientDTO versDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        dto.setTelephone(client.getTelephone());
        dto.setAdresse(client.getAdresse());
        return dto;
    }
    
    private Client versEntite(ClientDTO dto) {
        Client client = new Client();
        client.setNom(dto.getNom());
        client.setEmail(dto.getEmail());
        client.setTelephone(dto.getTelephone());
        client.setAdresse(dto.getAdresse());
        return client;
    }
}
