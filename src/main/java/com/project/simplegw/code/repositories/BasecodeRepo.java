package com.project.simplegw.code.repositories;

import java.util.List;

import com.project.simplegw.code.entities.Basecode;
import com.project.simplegw.code.vos.BasecodeType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BasecodeRepo extends JpaRepository<Basecode, Long> {
    List<Basecode> findByType(BasecodeType type);
}
