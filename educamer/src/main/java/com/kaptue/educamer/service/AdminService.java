package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.response.UserManagementDTO;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import com.kaptue.educamer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserManagementDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserManagementDTO::fromEntity)
            .collect(Collectors.toList());
    }
   
    @Transactional
    public void deleteUser(Long userId) {
        // Ajouter des vérifications (ex: ne pas supprimer le dernier admin)
        userRepository.deleteById(userId);
    }

    // --- MÉTHODE MISE À JOUR ---
    @Transactional
    public void setUserStatus(Long userId, boolean isEnabled) {
        // 1. Récupérer l'information sur l'administrateur qui effectue l'action
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            // Ce cas est peu probable si le endpoint est bien sécurisé, mais c'est une bonne pratique
            throw new IllegalStateException("Impossible de déterminer l'utilisateur courant.");
        }
        String currentAdminEmail = authentication.getName();

        // 2. Récupérer l'utilisateur cible (celui qu'on veut désactiver)
        User userToModify = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

        // 3. LA LOGIQUE DE PROTECTION : comparer l'email de l'admin et celui de l'utilisateur cible
        if (userToModify.getEmail().equals(currentAdminEmail)) {
            logger.warn("Tentative d'auto-désactivation par l'admin {}", currentAdminEmail);
            // On lance une exception pour empêcher l'opération et informer le client
            throw new IllegalStateException("Un administrateur ne peut pas désactiver son propre compte.");
        }
        
        // 4. Si la protection est passée, on effectue la modification
        userToModify.setEnabled(isEnabled);
        userRepository.save(userToModify);
        
        logger.info("L'admin {} a {} le compte de l'utilisateur {} (ID: {})",
                    currentAdminEmail, isEnabled ? "activé" : "désactivé", userToModify.getName(), userId);
    }

    
}