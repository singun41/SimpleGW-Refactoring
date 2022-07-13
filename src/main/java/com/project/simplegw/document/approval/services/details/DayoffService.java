package com.project.simplegw.document.approval.services.details;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.project.simplegw.document.approval.helpers.details.DayoffConverter;
import com.project.simplegw.document.approval.repositories.details.DayoffRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class DayoffService {
    private final DayoffRepo repo;
    private final DayoffConverter converter;
    

    public DayoffService(DayoffRepo repo, DayoffConverter converter) {
        this.repo = repo;
        this.converter = converter;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }


    
}
