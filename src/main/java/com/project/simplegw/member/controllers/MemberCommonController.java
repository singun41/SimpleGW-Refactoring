package com.project.simplegw.member.controllers;

import com.project.simplegw.member.services.MemberService;
import com.project.simplegw.system.helpers.ResponseConverter;
import com.project.simplegw.system.security.LoginUser;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MemberCommonController {
    private final MemberService memberService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public MemberCommonController(MemberService memberService) {
        this.memberService = memberService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- common ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @GetMapping("/{team}")
    public ResponseEntity<Object> getTeamMembers(@PathVariable String team) {
        return ResponseConverter.ok( memberService.getTeamMembers(team) );
    }

    @GetMapping("/{team}/without-me")
    public ResponseEntity<Object> getTeamMembersWithoutMe(@PathVariable String team, @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok( memberService.getTeamMembersWithoutMe(team, loginUser) );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- common ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
