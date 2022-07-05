package com.project.simplegw.member.controllers;

import com.project.simplegw.member.dtos.admin.receive.DtorMemberCreate;
import com.project.simplegw.member.dtos.admin.receive.DtorMemberUpdate;
import com.project.simplegw.member.dtos.admin.receive.DtorPwForceUpdate;
import com.project.simplegw.member.dtos.receive.DtorMyDetails;
import com.project.simplegw.member.dtos.receive.DtorPwChange;
import com.project.simplegw.member.services.MemberAdminService;
import com.project.simplegw.member.services.MemberClientService;
import com.project.simplegw.member.services.MemberService;
import com.project.simplegw.system.helpers.ResponseConverter;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ResponseMsg;
import com.project.simplegw.system.vos.Role;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class MemberController {
    private final MemberAdminService memberAdminService;
    private final MemberClientService memberClientService;
    private final MemberService memberService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public MemberController(MemberAdminService memberAdminService, MemberClientService memberClientService, MemberService memberService) {
        this.memberAdminService = memberAdminService;
        this.memberClientService = memberClientService;
        this.memberService = memberService;
        
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }

    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- admin ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @GetMapping(path = "/all", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> getMembers(@RequestParam boolean isRetired, @AuthenticationPrincipal LoginUser loginUser) {
        if( Role.ADMIN != loginUser.getMember().getRole() )
            return ResponseConverter.unauthorized();

        return ResponseConverter.ok(memberAdminService.getMembers(isRetired));
    }
    
    @PostMapping
    public ResponseEntity<Object> createMember(@Validated @RequestBody DtorMemberCreate dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if( Role.ADMIN != loginUser.getMember().getRole() )
            return ResponseConverter.unauthorized();

        if(result.hasErrors())
            return ResponseConverter.badRequest(result);

        return ResponseConverter.message( memberAdminService.create(dto), ResponseMsg.INSERTED );
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<Object> updateMember(@PathVariable Long memberId, @Validated @RequestBody DtorMemberUpdate dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if( Role.ADMIN != loginUser.getMember().getRole() )
            return ResponseConverter.unauthorized();

        if(result.hasErrors())
            return ResponseConverter.badRequest(result);

        return ResponseConverter.message( memberAdminService.update(memberId, dto), ResponseMsg.UPDATED );
    }

    @PatchMapping("/{memberId}/pw")
    public ResponseEntity<Object> updateMemberPw(@PathVariable Long memberId, @Validated @RequestBody DtorPwForceUpdate dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if( Role.ADMIN != loginUser.getMember().getRole())
            return ResponseConverter.unauthorized();

        if(result.hasErrors())
            return ResponseConverter.badRequest(result);

        return ResponseConverter.message( memberAdminService.updateMemberPw(memberId, dto), ResponseMsg.UPDATED );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- admin ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- user ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @GetMapping("/details")
    public ResponseEntity<Object> getMyDetails(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok(memberClientService.getMyDetails(loginUser));
    }

    @GetMapping(path ="/old-pw-matched", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> isOldPasswordMatched(@RequestParam String oldPw, @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok(memberClientService.isOldPasswordMatched(oldPw, loginUser));
    }

    @PatchMapping("/details")
    public ResponseEntity<Object> updateMyDetails(@Validated @RequestBody DtorMyDetails dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(memberClientService.updateMyDetails(dto, loginUser), ResponseMsg.UPDATED);
    }

    @PatchMapping("/password")
    public ResponseEntity<Object> updateMyPassword(@Validated @RequestBody DtorPwChange dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(memberClientService.updateMyPassword(dto, loginUser), ResponseMsg.UPDATED);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- user ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





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
