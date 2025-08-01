package com.kaptue.educamer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // Crée automatiquement le constructeur ChatResponse(String answer)
public class ChatResponse {

    private String answer;
}