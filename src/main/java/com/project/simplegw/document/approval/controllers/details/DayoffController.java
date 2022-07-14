package com.project.simplegw.document.approval.controllers.details;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.simplegw.document.approval.dtos.receive.details.dayoff.DtorDayoff;
import com.project.simplegw.document.approval.services.details.DayoffService;
import com.project.simplegw.system.helpers.ResponseConverter;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ResponseMsg;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping("/approval/dayoff")
public class DayoffController {
    private final DayoffService service;

    public DayoffController(DayoffService service) {
        this.service = service;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }

    


    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody DtorDayoff dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);

        return ResponseConverter.message( service.create(dto, loginUser), ResponseMsg.INSERTED );
    }
    

}
