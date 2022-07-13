package com.project.simplegw.document.controllers;

import com.project.simplegw.document.dtos.admin.receive.DtorEditorForm;
import com.project.simplegw.document.services.DocsFormService;
import com.project.simplegw.document.services.TempDocsService;
import com.project.simplegw.document.vos.EditorDocs;
import com.project.simplegw.system.helpers.ResponseConverter;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ResponseMsg;
import com.project.simplegw.system.vos.Role;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/docs")
public class DocsController {   // 페이지를 띄우는 건 ViewController 클래스가 담당한다.
    private final TempDocsService tempDocsService;
    private final DocsFormService formService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public DocsController(TempDocsService tempDocsService, DocsFormService formService) {
        this.tempDocsService = tempDocsService;
        this.formService = formService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }

    @GetMapping("/temp/list")
    public ResponseEntity<Object> getTempDocs(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok( tempDocsService.getTempDocs(loginUser) );
    }

    @GetMapping("/temp/count")
    public ResponseEntity<Object> getTempDocsCount(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.ok( tempDocsService.getTempDocsCount(loginUser) );
    }




    
    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- admin ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @GetMapping("/form/{docs}")
    public ResponseEntity<Object> getForm(@PathVariable EditorDocs docs, @AuthenticationPrincipal LoginUser loginUser) {
        if( Role.ADMIN != loginUser.getMember().getRole() )
            ResponseConverter.unauthorized();

        return ResponseConverter.ok( formService.getForm(docs) );
    }


    @PatchMapping("/form")
    public ResponseEntity<Object> saveForm(@RequestBody DtorEditorForm dto, @AuthenticationPrincipal LoginUser loginUser) {
        if( Role.ADMIN != loginUser.getMember().getRole() )
            ResponseConverter.unauthorized();

        return ResponseConverter.message( formService.saveForm(dto) , ResponseMsg.SAVED );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- admin ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
