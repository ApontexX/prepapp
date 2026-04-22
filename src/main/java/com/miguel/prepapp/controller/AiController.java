package com.miguel.prepapp.controller;

import com.miguel.prepapp.model.Block;
import com.miguel.prepapp.model.Folder;
import com.miguel.prepapp.repository.BlockRepository;
import com.miguel.prepapp.repository.FolderRepository;
import com.miguel.prepapp.service.ClaudeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final ClaudeService claudeService;
    private final FolderRepository folderRepository;
    private final BlockRepository blockRepository;

    @PostMapping("/analyze/{folderId}")
    public ResponseEntity<?> analyzeFolder(
            @PathVariable Long folderId,
            @RequestBody Map<String, String> body,
            Authentication auth) {

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));

        if (!folder.getUser().getEmail().equals(auth.getName())) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        List<Block> blocks = blockRepository.findByFolderIdOrderByPosition(folderId);
        String context = body.getOrDefault("context", "");

        String blocksContent = blocks.stream()
                .map(b -> "### " + b.getTitle() + "\n" + b.getContent())
                .collect(Collectors.joining("\n\n"));

        String systemPrompt = """
                Eres un asistente experto en preparación para entrevistas de trabajo y exposiciones.
                Tu rol es ayudar al usuario a prepararse de la mejor manera posible.
                Responde siempre en español, de forma clara y estructurada.
                Da consejos prácticos y específicos basados en el contenido del usuario.
                """;

        String userMessage = """
                Tengo una carpeta llamada "%s" con los siguientes apuntes:
                
                %s
                
                %s
                
                Por favor:
                1. Analiza cada bloque y dame recomendaciones específicas para mejorarlo
                2. Dime qué preguntas me podrían hacer basándote en este contenido
                3. Dime qué información importante podría estar faltando
                4. Dame consejos generales para mi preparación
                """.formatted(
                folder.getName(),
                blocksContent,
                context.isEmpty() ? "" : "Contexto adicional: " + context
        );

        String response = claudeService.sendMessage(systemPrompt, userMessage);
        return ResponseEntity.ok(Map.of("response", response));
    }

    @PostMapping("/chat/{folderId}")
    public ResponseEntity<?> chat(
            @PathVariable Long folderId,
            @RequestBody Map<String, String> body,
            Authentication auth) {

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));

        if (!folder.getUser().getEmail().equals(auth.getName())) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        List<Block> blocks = blockRepository.findByFolderIdOrderByPosition(folderId);
        String question = body.getOrDefault("question", "");
        String context = body.getOrDefault("context", "");

        String blocksContent = blocks.stream()
                .map(b -> "### " + b.getTitle() + "\n" + b.getContent())
                .collect(Collectors.joining("\n\n"));

        String systemPrompt = """
                Eres un asistente experto en preparación para entrevistas de trabajo y exposiciones.
                Tienes acceso a los apuntes del usuario y debes responder sus preguntas
                teniendo en cuenta ese contexto.
                Responde siempre en español, de forma clara y útil.
                """;

        String userMessage = """
                Mis apuntes para "%s":
                
                %s
                
                %s
                
                Mi pregunta es: %s
                """.formatted(
                folder.getName(),
                blocksContent,
                context.isEmpty() ? "" : "Contexto de la oportunidad: " + context,
                question
        );

        String response = claudeService.sendMessage(systemPrompt, userMessage);
        return ResponseEntity.ok(Map.of("response", response));
    }
}
