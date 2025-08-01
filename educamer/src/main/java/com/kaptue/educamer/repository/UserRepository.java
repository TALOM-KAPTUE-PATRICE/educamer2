package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Trouve un utilisateur par son adresse email.
     * C'est la méthode la plus importante pour le processus de connexion (login).
     * Optional<User> est utilisé pour éviter les NullPointerException si l'utilisateur n'est pas trouvé.
     *
     * @param email L'email de l'utilisateur à rechercher.
     * @return un Optional contenant l'utilisateur s'il est trouvé, sinon un Optional vide.
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Vérifie si un utilisateur avec cet email existe déjà.
     * C'est plus performant que de récupérer l'entité complète juste pour une vérification.
     * Utile lors de l'inscription pour éviter les doublons.
     *
     * @param email L'email à vérifier.
     * @return true si l'email existe, false sinon.
     */
    boolean existsByEmail(String email);
    
    /**
     * Exemple de requête plus complexe si nécessaire.
     * Trouve un utilisateur par son email, et charge en même temps certaines de ses relations 
     * pour éviter le problème des N+1 requêtes (chargement EAGER).
     *
     * @param email L'email de l'utilisateur.
     * @return un Optional de l'utilisateur avec ses relations chargées.
     */
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserWithDetailsByEmail(String email); // Le nom peut être ce que vous voulez
}