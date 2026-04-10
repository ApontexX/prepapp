package com.miguel.prepapp.service;

import com.miguel.prepapp.model.Block;
import com.miguel.prepapp.model.Folder;
import com.miguel.prepapp.repository.BlockRepository;
import com.miguel.prepapp.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final FolderRepository folderRepository;

    public List<Block> getBlocksByFolder(Long folderId, String email) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));

        if (!folder.getUser().getEmail().equals(email)) {
            throw new RuntimeException("No tienes permiso para ver esta carpeta");
        }

        return blockRepository.findByFolderIdOrderByPosition(folderId);
    }

    public Block createBlock(Long folderId, String email, String title, String content) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));

        if (!folder.getUser().getEmail().equals(email)) {
            throw new RuntimeException("No tienes permiso para agregar bloques a esta carpeta");
        }

        Block block = new Block();
        block.setTitle(title);
        block.setContent(content);
        block.setFolder(folder);
        block.setPosition(blockRepository.findByFolderIdOrderByPosition(folderId).size());
        return blockRepository.save(block);
    }

    public Block updateBlock(Long blockId, String email, String title, String content) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Bloque no encontrado"));

        if (!block.getFolder().getUser().getEmail().equals(email)) {
            throw new RuntimeException("No tienes permiso para editar este bloque");
        }

        block.setTitle(title);
        block.setContent(content);
        return blockRepository.save(block);
    }

    public void deleteBlock(Long blockId, String email) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Bloque no encontrado"));

        if (!block.getFolder().getUser().getEmail().equals(email)) {
            throw new RuntimeException("No tienes permiso para eliminar este bloque");
        }

        blockRepository.delete(block);
    }
}