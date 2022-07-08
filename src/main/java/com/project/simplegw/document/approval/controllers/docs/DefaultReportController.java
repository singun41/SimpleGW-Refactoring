package com.project.simplegw.document.approval.controllers.docs;

import com.project.simplegw.document.approval.dtos.receive.docs.DtorDefaultReport;
import com.project.simplegw.document.approval.dtos.receive.docs.DtorTempDefaultReport;
import com.project.simplegw.document.approval.services.docs.DefaultReportService;
import com.project.simplegw.system.helpers.ResponseConverter;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ResponseMsg;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/approval/default")
public class DefaultReportController {
    private final DefaultReportService service;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public DefaultReportController(DefaultReportService service) {
        this.service = service;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }




    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody DtorDefaultReport dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            service.create(dto, loginUser), ResponseMsg.INSERTED
        );
    }


    @PatchMapping("/{docsId}")
    public ResponseEntity<Object> update(@PathVariable Long docsId, @Validated @RequestBody DtorDefaultReport dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            service.update(docsId, dto, loginUser), ResponseMsg.UPDATED
        );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @PostMapping("/temp")
    public ResponseEntity<Object> createTemp(@Validated @RequestBody DtorTempDefaultReport dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            service.createTemp(dto, loginUser), ResponseMsg.INSERTED
        );
    }

    @PatchMapping("/temp/{docsId}")
    public ResponseEntity<Object> updateTemp(@PathVariable Long docsId, @Validated @RequestBody DtorTempDefaultReport dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            service.updateTemp(docsId, dto, loginUser), ResponseMsg.INSERTED
        );
    }

    @DeleteMapping("/temp/{docsId}")
    public ResponseEntity<Object> deleteTemp(@PathVariable Long docsId, @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.message(
            service.deleteTemp(docsId, loginUser), ResponseMsg.DELETED
        );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
