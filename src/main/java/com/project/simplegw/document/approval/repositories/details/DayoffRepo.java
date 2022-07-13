package com.project.simplegw.document.approval.repositories.details;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.simplegw.document.approval.entities.details.Dayoff;

@Repository
public interface DayoffRepo extends JpaRepository<Dayoff, Long> {
    List<Dayoff> findByDocsId(Long docsId);
}
