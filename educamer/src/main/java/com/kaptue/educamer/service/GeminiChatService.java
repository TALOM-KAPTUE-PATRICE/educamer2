package com.kaptue.educamer.service;


import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse; // Utilisez votre exception custom
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.kaptue.educamer.exception.ExternalApiException;

@Service
public class GeminiChatService {

    @Value("${gemini.project.id}")
    private String projectId;

    @Value("${gemini.location}")
    private String location;

    @Value("${gemini.model.name}")
    private String modelName;

    public String generateContent(String userQuery) {
        // L'initialisation du client Vertex AI se fait maintenant ici
        try (VertexAI vertexAi = new VertexAI(projectId, location)) {
            
            String systemInstruction = "Tu es 'EduBot', un tuteur virtuel pour la plateforme EduCamer. "
                + "Ton rôle est d'aider les élèves camerounais dans leurs études. "
                + "Réponds uniquement aux questions de nature éducative (maths, physique, histoire, etc.). "
                + "Si une question n'est pas éducative, refuse poliment en disant : "
                + "'Je suis EduBot, votre tuteur virtuel. Mon rôle est de vous aider dans vos études. Je ne peux pas répondre à cette question.' "
                + "Tes réponses doivent être claires, encourageantes et adaptées à un niveau scolaire.";

            GenerativeModel model = new GenerativeModel(modelName, vertexAi)
                .withSystemInstruction(com.google.cloud.vertexai.api.Content.newBuilder()
                    .addParts(com.google.cloud.vertexai.api.Part.newBuilder().setText(systemInstruction).build())
                    .build());

            GenerateContentResponse response = model.generateContent(userQuery);
            
            return ResponseHandler.getText(response);
        } catch (IOException e) {
            // On encapsule l'erreur technique dans une exception métier
            // qui sera gérée par le GlobalExceptionHandler.
            // Cela évite les try-catch dans le contrôleur.
            throw new ExternalApiException("Erreur de communication avec le service d'IA de Google. Détails: " + e.getMessage());
        }
    }
}