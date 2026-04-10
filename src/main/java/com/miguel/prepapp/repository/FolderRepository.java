package com.miguel.prepapp.repository;

import com.miguel.prepapp.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByUserIdOrderByPosition(Long userId);
}