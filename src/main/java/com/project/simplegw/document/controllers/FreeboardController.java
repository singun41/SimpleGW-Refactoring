package com.project.simplegw.document.controllers;

import java.time.LocalDate;

import com.project.simplegw.document.dtos.receive.DtorDocs;
import com.project.simplegw.document.services.FreeboardService;
import com.project.simplegw.system.helpers.ResponseConverter;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ResponseMsg;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/freeboard")
public class FreeboardController {
    private final FreeboardService freeboardService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public FreeboardController(FreeboardService freeboardService) {
        this.freeboardService = freeboardService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    
    @GetMapping("/main-list")
    public ResponseEntity<Object> getMainPageList() {
        return ResponseConverter.ok(freeboardService.getMainPageList());
    }


    @GetMapping(path = "/list", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> getList(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate dateFrom, @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate dateTo) {
        return ResponseConverter.ok(freeboardService.getList(dateFrom, dateTo));
    }





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody DtorDocs dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            freeboardService.create(dto, loginUser), ResponseMsg.INSERTED
        );
    }

    @PatchMapping("/{docsId}")
    public ResponseEntity<Object> update(@PathVariable Long docsId, @Validated @RequestBody DtorDocs dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            freeboardService.update(docsId, dto, loginUser), ResponseMsg.UPDATED
        );
    }

    @DeleteMapping("/{docsId}")
    public ResponseEntity<Object> delete(@PathVariable Long docsId, @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.message(
            freeboardService.delete(docsId, loginUser), ResponseMsg.DELETED
        );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //




    
    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @PostMapping("/temp")
    public ResponseEntity<Object> createTemp(@Validated @RequestBody DtorDocs dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            freeboardService.createTemp(dto, loginUser), ResponseMsg.INSERTED
        );
    }

    @PatchMapping("/temp/{docsId}")
    public ResponseEntity<Object> updateTemp(@PathVariable Long docsId, @Validated @RequestBody DtorDocs dto, BindingResult result, @AuthenticationPrincipal LoginUser loginUser) {
        if(result.hasErrors())
            return ResponseConverter.badRequest(result);
        
        return ResponseConverter.message(
            freeboardService.updateTemp(docsId, dto, loginUser), ResponseMsg.UPDATED
        );
    }

    @DeleteMapping("/temp/{docsId}")
    public ResponseEntity<Object> deleteTemp(@PathVariable Long docsId, @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.message(
            freeboardService.deleteTemp(docsId, loginUser), ResponseMsg.DELETED
        );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
