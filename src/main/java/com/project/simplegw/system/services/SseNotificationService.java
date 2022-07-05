package com.project.simplegw.system.services;

import java.util.Map;

import com.project.simplegw.system.vos.SseDataType;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SseNotificationService {
    private final SseService sseService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public SseNotificationService(SseService sseService) {
        this.sseService = sseService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }

    
    public void sendNotification(Long memberId) {
        sseService.send(memberId, Map.of(SseDataType.NOTIFICATION.name(), "1"));
    }
}
