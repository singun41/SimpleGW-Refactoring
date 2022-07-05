package com.project.simplegw.document.approval.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.simplegw.document.approval.entities.ApprovalStatus;

public interface ApprovalStatusRepo extends JpaRepository<ApprovalStatus, Long> {
    Optional<ApprovalStatus> findByDocsId(Long docsId);
}
