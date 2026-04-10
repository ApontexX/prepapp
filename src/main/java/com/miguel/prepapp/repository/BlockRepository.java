package com.miguel.prepapp.repository;

import com.miguel.prepapp.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByFolderIdOrderByPosition(Long folderId);
}