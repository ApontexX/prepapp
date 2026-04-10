package com.miguel.prepapp.controller;

import com.miguel.prepapp.model.Block;
import com.miguel.prepapp.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<List<Block>> getBlocks(@PathVariable Long folderId,
                                                  Authentication auth) {
        return ResponseEntity.ok(blockService.getBlocksByFolder(folderId, auth.getName()));
    }

    @PostMapping("/folder/{folderId}")
    public ResponseEntity<Block> createBlock(@PathVariable Long folderId,
                                              @RequestBody Map<String, String> body,
                                              Authentication auth) {
        Block block = blockService.createBlock(
                folderId,
                auth.getName(),
                body.get("title"),
                body.get("content")
        );
        return ResponseEntity.ok(block);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Block> updateBlock(@PathVariable Long id,
                                              @RequestBody Map<String, String> body,
                                              Authentication auth) {
        Block block = blockService.updateBlock(
                id,
                auth.getName(),
                body.get("title"),
                body.get("content")
        );
        return ResponseEntity.ok(block);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlock(@PathVariable Long id, Authentication auth) {
        blockService.deleteBlock(id, auth.getName());
        return ResponseEntity.ok(Map.of("message", "Bloque eliminado correctamente"));
    }
}