package com.kaptue.educamer.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter @AllArgsConstructor
public class LinkingCodeResponse {
    private String code;
    private String expiryMessage; // ex: "Ce code expire dans 10 minutes."
}