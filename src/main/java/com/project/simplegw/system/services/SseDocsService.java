package com.project.simplegw.system.services;

import com.project.simplegw.system.vos.SseDataType;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SseDocsService {
    private final SseService sseService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public SseDocsService(SseService sseService) {
        this.sseService = sseService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    @Async   // 비동기로 처리하면 호출자와 별개로 동작하므로 유저에게 쓰레드 지연 영향을 주지 않는다.
    public void sendNotice() {   // called from NoticeService
        // 공지사항 등록/수정 후 sse로 접속해있는 모든 유저에게 알림을 전달한다.
        // 등록/수정 후 즉시 알림을 전달하면 갱신된 내용으로 리스트를 받지 못하는 경우가 있어 쓰레드를 잠깐 지연시킨다.
        try {
            Thread.sleep(1000L);
        } catch(Exception e) {
            e.printStackTrace();
            log.warn("sendNotice() method thread delay failed..");
        }

        sseService.sendToAll(SseDataType.NOTICE);
    }

    
    @Async
    public void sendFreeboard() {   // called from FreeboardService
        // sendNotice() 메서드와 같은 이유
        try {
            Thread.sleep(1000L);
        } catch(Exception e) {
            e.printStackTrace();
            log.warn("sendFreeboard() method thread delay failed..");
        }

        sseService.sendToAll(SseDataType.FREEBOARD);
    }
}
