package com.project.simplegw.member.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.simplegw.member.dtos.receive.DtorProfile;
import com.project.simplegw.member.dtos.receive.DtorPwChange;
import com.project.simplegw.member.services.MemberAddOnService;
import com.project.simplegw.member.services.MemberClientService;
import com.project.simplegw.system.helpers.ResponseConverter;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ResponseMsg;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MemberClientController {
    private final MemberClientService service;
    private final MemberAddOnService addOnService;

    public MemberClientController(MemberClientService service, MemberAddOnService addOnService) {
        this.service = service;
        this.addOnService = addOnService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }




    
    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok( service.getProfile(loginUser) );
    }


    @GetMapping(path ="/old-pw-matched", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> isOldPasswordMatched(@RequestParam String oldPw, @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok( service.isOldPasswordMatched(oldPw, loginUser) );
    }


    @PatchMapping("/profile")
    public ResponseEntity<Object> updateProfile(@Validated @RequestBody DtorProfile dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            service.updateProfile(dto, loginUser), ResponseMsg.UPDATED
        );
    }


    @PatchMapping("/password")
    public ResponseEntity<Object> updateMyPassword(@Validated @RequestBody DtorPwChange dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            service.updateMyPassword(dto, loginUser), ResponseMsg.UPDATED
        );
    }


    @GetMapping("/dayoff-count")
    public ResponseEntity<Object> getDayoffCount(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok( addOnService.getDayoffCount(loginUser) );
    }
}
