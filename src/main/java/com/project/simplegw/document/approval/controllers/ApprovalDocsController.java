package com.project.simplegw.document.approval.controllers;

import com.project.simplegw.document.approval.services.ApprovalCountService;
import com.project.simplegw.document.approval.services.ApprovalDocsService;
import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.system.helpers.ResponseConverter;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ResponseMsg;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/approval")
public class ApprovalDocsController {
    private final ApprovalDocsService approvalDocsService;
    private final ApprovalCountService countService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public ApprovalDocsController(ApprovalDocsService approvalDocsService, ApprovalCountService countService) {
        this.approvalDocsService = approvalDocsService;
        this.countService = countService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재자 승인/반려 처리 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @PatchMapping("/confirmed/{type}/{docsId}")
    public ResponseEntity<Object> confirmed(@PathVariable DocsType type, @PathVariable Long docsId, @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.message(
            approvalDocsService.confirmed(type, docsId, loginUser), ResponseMsg.CONFIRMED
        );
    }

    @PatchMapping("/rejected/{type}/{docsId}")
    public ResponseEntity<Object> rejected(@PathVariable DocsType type, @PathVariable Long docsId, @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.message(
            approvalDocsService.rejected(type, docsId, loginUser), ResponseMsg.REJECTED
        );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재자 승인/반려 처리 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //




    @GetMapping("/proceed-cnt")
    public ResponseEntity<Object> getProceedDocsCnt(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok( countService.getProceedDocsCnt(loginUser) );
    }

    @GetMapping("/approver-cnt")
    public ResponseEntity<Object> getApproverDocsCnt(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok( countService.getApproverDocsCnt(loginUser) );
    }

    @GetMapping("/referrer-cnt")
    public ResponseEntity<Object> getReferrerDocsCnt(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok( countService.getReferrerDocsCnt(loginUser) );
    }
}
