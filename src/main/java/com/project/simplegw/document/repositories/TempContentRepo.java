package com.project.simplegw.document.repositories;

import java.util.Optional;

import com.project.simplegw.document.entities.TempContent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TempContentRepo extends JpaRepository<TempContent, Long> {
    Optional<TempContent> findByTempDocsId(Long tempDocsId);
}
