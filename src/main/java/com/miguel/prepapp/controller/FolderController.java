package com.miguel.prepapp.controller;

import com.miguel.prepapp.model.Folder;
import com.miguel.prepapp.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @GetMapping
    public ResponseEntity<List<Folder>> getFolders(Authentication auth) {
        return ResponseEntity.ok(folderService.getFoldersByUser(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<Folder> createFolder(@RequestBody Map<String, String> body,
                                                Authentication auth) {
        Folder folder = folderService.createFolder(
                auth.getName(),
                body.get("name"),
                body.get("description")
        );
        return ResponseEntity.ok(folder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long id,
                                                @RequestBody Map<String, String> body,
                                                Authentication auth) {
        Folder folder = folderService.updateFolder(
                id,
                auth.getName(),
                body.get("name"),
                body.get("description")
        );
        return ResponseEntity.ok(folder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long id, Authentication auth) {
        folderService.deleteFolder(id, auth.getName());
        return ResponseEntity.ok(Map.of("message", "Carpeta eliminada correctamente"));
    }
}