package com.miguel.prepapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

@Service
public class ClaudeService {

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ClaudeService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.anthropic.com")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public String sendMessage(String systemPrompt, String userMessage) {
        try {
            Map<String, Object> requestBody = Map.of(
                "model", "claude-haiku-4-5",
                "max_tokens", 1024,
                "system", systemPrompt,
                "messages", List.of(
                    Map.of("role", "user", "content", userMessage)
                )
            );

            String response = webClient.post()
                    .uri("/v1/messages")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .header("content-type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Map responseMap = objectMapper.readValue(response, Map.class);
            List content = (List) responseMap.get("content");
            Map firstContent = (Map) content.get(0);
            return (String) firstContent.get("text");

        } catch (Exception e) {
            throw new RuntimeException("Error al llamar a Claude: " + e.getMessage());
        }
    }
}