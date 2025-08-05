# Étape 1: Utiliser une image de base avec Maven et Java 21 pour construire le projet
# On utilise une image multi-architecture pour être compatible avec les processeurs ARM (comme les Mac M1/M2)
FROM --platform=linux/amd64 maven:3.9.6-eclipse-temurin-21 AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers de configuration Maven pour télécharger les dépendances
COPY pom.xml .

# Télécharger les dépendances (cette couche sera mise en cache si pom.xml ne change pas)
RUN mvn dependency:go-offline

# Copier le reste du code source de l'application
COPY src ./src

# Construire l'application et créer le fichier JAR exécutable
# -DskipTests saute les tests unitaires pour un build plus rapide en déploiement
RUN mvn package -DskipTests

# Étape 2: Utiliser une image Java 21 légère pour l'exécution
# L'image JRE (Java Runtime Environment) est plus petite que le JDK complet
FROM --platform=linux/amd64 eclipse-temurin:21-jre-jammy

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR construit depuis l'étape de build précédente
COPY --from=build /app/target/*.jar app.jar

# Exposer le port sur lequel l'application va écouter
# Render utilisera cette information, mais définira son propre port externe.
EXPOSE 8081

# La commande pour lancer l'application lorsque le conteneur démarre
ENTRYPOINT ["java", "-jar", "app.jar"]