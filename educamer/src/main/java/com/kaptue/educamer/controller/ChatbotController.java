package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.ChatRequest;
import com.kaptue.educamer.dto.response.ChatResponse;
import com.kaptue.educamer.service.GeminiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")

public class ChatbotController {

    @Autowired
    private GeminiChatService geminiChatService;

    @PostMapping("/ask")
    @PreAuthorize("hasRole('STUDENT')") 
    public ResponseEntity<ChatResponse> askQuestion(@RequestBody ChatRequest request) {
        // Plus besoin de try-catch ici, le GlobalExceptionHandler s'en occupe.
        String responseText = geminiChatService.generateContent(request.getQuestion());
        return ResponseEntity.ok(new ChatResponse(responseText));
    }

}