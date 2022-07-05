package com.project.simplegw.system.services;

import java.util.Collection;
import java.util.Map;

import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.system.vos.SseApprovalType;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SseApprovalService {
    private final SseService sseService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public SseApprovalService(SseService sseService) {
        this.sseService = sseService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }
    

    public void sendToApprovalSubmitter(SseApprovalType type, Docs docs) {
        if(docs.getWriterId() == null)
            return;
        
        switch(type) {
            case CONFIRMED:
            case REJECTED:
                sseService.send(
                    docs.getWriterId(),
                    Map.of(
                        type.name(), "1",
                        "docsId", docs.getId().toString(),
                        "type", docs.getType().getTitle(),
                        "title", docs.getTitle()
                    )
                );
                return;

            default: return;
        }
    }


    public void sendToCurrentApprover(Long memberId) {
        if(memberId == null)
            return;

        sseService.send(memberId, Map.of(SseApprovalType.APPROVER.name(), "1"));
    }


    public void sendToAllReferrers(Collection<Long> referrers, SseApprovalType type) {
        if(referrers == null)
            return;
        
        referrers.forEach(e -> sseService.send(e, Map.of(SseApprovalType.REFERRER.name(), "1")));
    }


    public void sendToReferrer(Long memberId) {
        if(memberId == null)
            return;

        sseService.send(memberId, Map.of(SseApprovalType.REFERRER.name(), "1"));
    }
}
