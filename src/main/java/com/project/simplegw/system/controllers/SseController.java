package com.project.simplegw.system.controllers;

import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.services.SseService;
import com.project.simplegw.system.vos.SseDisconnType;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sse")
public class SseController {   // Server Sent Event Controller
    private final SseService sseService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public SseController(SseService sseService) {
        this.sseService = sseService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }

    @GetMapping("/connect")
    public SseEmitter connect(@AuthenticationPrincipal LoginUser loginUser) {
        return sseService.connect(loginUser);
    }

    @GetMapping("/disconnect")
    public void remove(@AuthenticationPrincipal LoginUser loginUser) {
        sseService.remove(loginUser, SseDisconnType.CONN_CLOSE);
    }
}
