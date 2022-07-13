package com.project.simplegw.document.approval.controllers.details;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.simplegw.document.approval.services.details.DayoffService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/approval/dayoff")
public class DayoffController {
    private final DayoffService service;

    public DayoffController(DayoffService service) {
        this.service = service;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }

    
}
