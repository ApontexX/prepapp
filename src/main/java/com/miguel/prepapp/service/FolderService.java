package com.miguel.prepapp.service;

import com.miguel.prepapp.model.Folder;
import com.miguel.prepapp.model.User;
import com.miguel.prepapp.repository.FolderRepository;
import com.miguel.prepapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public List<Folder> getFoldersByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return folderRepository.findByUserIdOrderByPosition(user.getId());
    }

    public Folder createFolder(String email, String name, String description) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Folder folder = new Folder();
        folder.setName(name);
        folder.setDescription(description);
        folder.setUser(user);
        folder.setPosition(folderRepository.findByUserIdOrderByPosition(user.getId()).size());
        return folderRepository.save(folder);
    }

    public Folder updateFolder(Long folderId, String email, String name, String description) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));

        if (!folder.getUser().getEmail().equals(email)) {
            throw new RuntimeException("No tienes permiso para editar esta carpeta");
        }

        folder.setName(name);
        folder.setDescription(description);
        return folderRepository.save(folder);
    }

    public void deleteFolder(Long folderId, String email) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));

        if (!folder.getUser().getEmail().equals(email)) {
            throw new RuntimeException("No tienes permiso para eliminar esta carpeta");
        }

        folderRepository.delete(folder);
    }
}