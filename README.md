# EduCamer - Plateforme d'Accompagnement Scolaire en Ligne


**EduCamer** est une application web full-stack moderne conçue pour révolutionner l'accompagnement scolaire au Cameroun. Elle offre un écosystème complet et interactif pour les élèves, les parents, les instructeurs et les tuteurs, en alignant le contenu pédagogique sur le programme scolaire national.

Ce projet a été développé pour démontrer la maîtrise des technologies modernes telles que **Spring Boot** pour le backend et **Angular** pour le frontend, en mettant l'accent sur une architecture sécurisée, une logique métier robuste et une expérience utilisateur fluide.

---

## 🚀 Fonctionnalités Clés

EduCamer est une plateforme riche en fonctionnalités, organisée autour des besoins de chaque utilisateur :

### Pour les Élèves 👨‍🎓
*   **Catalogue de Cours :** Explorez une large gamme de cours conformes au programme, de la 3ème à la Terminale.
*   **Parcours d'Apprentissage Séquentiel :** Progressez leçon par leçon, avec des quiz de validation pour assurer la maîtrise des concepts.
*   **Soumission de Devoirs :** Uploadez vos devoirs (fichiers PDF, images) directement sur la plateforme.
*   **Suivi Personnalisé :** Visualisez votre progression, vos notes et vos statistiques sur un tableau de bord intuitif.
*   **Tutorat à la Demande :** Bloqué sur un exercice ? Demandez l'aide d'un tuteur qualifié en un clic.
*   **Forum d'Entraide :** Échangez avec d'autres élèves et les instructeurs dans des forums de discussion dédiés à chaque cours.

### Pour les Parents 👩‍👦
*   **Liaison de Compte Sécurisée :** Liez votre compte à celui de vos enfants pour un suivi discret et efficace.
*   **Tableau de Bord de Suivi :** Accédez en temps réel aux notes, à la progression et aux activités de vos enfants.
*   **Communication Facilitée :** Restez informé et soutenez activement le parcours scolaire de vos enfants.

### Pour les Instructeurs & Tuteurs 🧑‍🏫
*   **Éditeur de Cours Puissant :** Créez des cours complets avec des leçons, des ressources multimédias (vidéos, PDF), des devoirs et des quiz.
*   **Gestion des Contenus :** Organisez vos leçons par glisser-déposer, mettez à jour vos ressources et publiez vos cours en un clic.
*   **Suivi des Performances :** Analysez les statistiques de vos cours, suivez la progression de vos élèves et notez les devoirs soumis.
*   **Modération de Forum :** Animez et modérez les discussions pour maintenir un environnement d'apprentissage sain.

### Pour les Administrateurs ⚙️
*   **Gestion des Utilisateurs :** Validez, gérez et supervisez tous les comptes de la plateforme.

---

## 🛠️ Stack Technique

Ce projet met en œuvre une architecture moderne et robuste :

### Backend (API RESTful)
*   **Framework :** [Spring Boot 3](https://spring.io/projects/spring-boot)
*   **Sécurité :** [Spring Security 6](https://spring.io/projects/spring-security) avec authentification par **JWT (JSON Web Tokens)**.
*   **Base de Données :** [Spring Data JPA](https://spring.io/projects/spring-data-jpa) (Hibernate) avec **PostgreSQL** en production (Render) et **MySQL** en développement.

*   **Stockage de Fichiers :** [Cloudinary](https://cloudinary.com/) pour un stockage cloud robuste des avatars, PDF, vidéos et autres ressources.
*   **Gestion des Rôles :** Sécurité fine basée sur les rôles (`hasRole`) et les permissions granulaires (`hasAuthority`).

### Frontend (Single Page Application)
*   **Framework :** [Angular 19+](https://angular.io/)
*   **UI/UX :** [Angular Material](https://material.angular.io/) pour une interface utilisateur moderne, responsive et accessible.
*   **Gestion d'État :** [RxJS](https://rxjs.dev/) avec des `BehaviorSubject` pour une gestion réactive de l'état de l'utilisateur.
*   **Communication API :** Intercepteurs HTTP pour l'injection automatique des tokens JWT et la gestion centralisée des erreurs.
*   **Routage :** Gardes de route (`CanActivate`) pour protéger les pages en fonction de l'état d'authentification et du rôle de l'utilisateur.

### Déploiement
*   **Backend :** Déployé sur [Render](https://render.com/).
*   **Frontend :** Déployé sur [Netlify](https://www.netlify.com/), avec configuration de proxy pour une communication transparente avec le backend.

---

## ⚙️ Installation et Lancement

Pour lancer ce projet en local, suivez ces étapes :

### Prérequis
*   [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
*   [Maven](https://maven.apache.org/)
*   [Node.js et npm](https://nodejs.org/)
*   [Angular CLI](https://angular.io/cli)
*   Un serveur MySQL (ex: WampServer, XAMPP)
*   Un compte [Cloudinary](https://cloudinary.com/)

### 1. Execution du projet
```bash
#  Clonez le dépôt 
git clone https://github.com/TALOM-KAPTUE-PATRICE/educamer2
cd educamer2

#  Configurez votre base de données
# Créez une base de données nommée 'educamer_db' dans votre serveur MySQL.

#  Configurez les variables d'environnement
# Dans `src/main/resources/application-dev.properties`, mettez à jour :
# - spring.datasource.username et password
# - jwt.secret.key (générez une longue chaîne aléatoire)
# - cloudinary.url (votre URL Cloudinary)
# - MAIL_USERNAME et MAIL_PASSWORD (pour l'envoi d'emails)

#  Lancez l'application
mvn spring-boot:run

#  Installez les dépendances
npm install

#  Configurez le proxy
# Le fichier `proxy.conf.json` est déjà configuré pour pointer vers `localhost:8081`.

#  Lancez le serveur de développement
ng serve

