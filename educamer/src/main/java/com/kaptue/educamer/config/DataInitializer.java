package com.kaptue.educamer.config;

import com.kaptue.educamer.entity.Admin;
import com.kaptue.educamer.repository.AdminRepository;
import com.kaptue.educamer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired private AdminRepository adminRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // Injecter les valeurs depuis application.properties
    @Value("${admin.bootstrap.enabled:false}") // Par défaut à false pour la sécurité
    private boolean enabled;
    
    @Value("${admin.bootstrap.name}")
    private String adminName;

    @Value("${admin.bootstrap.email}")
    private String adminEmail;

    @Value("${admin.bootstrap.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        // Ne rien faire si la fonctionnalité est désactivée dans les propriétés
        if (!enabled) {
            logger.info("Initialisation de l'admin désactivée.");
            return;
        }

        // Vérifier si un admin existe déjà
        if (adminRepository.count() > 0) {
            logger.info("Un ou plusieurs comptes admin existent déjà. Aucune action requise.");
            return;
        }
        
        // Vérifier si un utilisateur avec cet email existe déjà (double sécurité)
        if (userRepository.existsByEmail(adminEmail)) {
            logger.warn("Un utilisateur avec l'email {} existe déjà, mais n'est pas un admin. Vérification manuelle requise.", adminEmail);
            return;
        }

        // Si aucun admin n'existe, on crée le premier
        logger.info("Aucun compte admin trouvé. Création du compte admin initial...");
        
        Admin initialAdmin = new Admin();
        initialAdmin.setName(adminName);
        initialAdmin.setEmail(adminEmail);
        initialAdmin.setPassword(passwordEncoder.encode(adminPassword));

        adminRepository.save(initialAdmin);
        
        logger.info("************************************************************");
        logger.info("COMPTE ADMIN CRÉÉ AVEC SUCCÈS !");
        logger.info("Email: {}", adminEmail);
        logger.info("Mot de passe: [CELUI DANS VOTRE application.properties]");
        logger.info("Veuillez vous connecter et changer ce mot de passe si nécessaire.");
        logger.info("************************************************************");
    }
}