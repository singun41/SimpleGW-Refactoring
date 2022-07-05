package com.project.simplegw.system.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.project.simplegw.system.entities.Notification;
import com.project.simplegw.system.repositories.NotificationRepo;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.Constants;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class NotificationService {   // 알림 카운트, 알림 내용 생성 및 읽기를 제공하는 공용 서비스, 디테일한 내용 생성은 XxxNotificationService 클래스에 작성하고 호출.
    private final NotificationRepo notificationRepo;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }


    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- called from NotificationController ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public long getUncheckedNotificationsCount(LoginUser loginUser) {
        return notificationRepo.countByMemberIdAndChecked(loginUser.getMember().getId(), false);
    }

    public List<String> getNotifications(LoginUser loginUser) {
        return notificationRepo.findByMemberIdOrderByIdDesc(loginUser.getMember().getId()).stream().map(Notification::getContent).collect(Collectors.toList());
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- called from NotificationController ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- same package services ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    void create(Long memberId, String content) {
        if(memberId == null || content == null)
            return;
        
        Notification notification = Notification.builder().content(content).checked(false).memberId(memberId).build();

        try {
            notificationRepo.save(notification);

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("create exception.");
            log.warn("paramters: {}, {}", memberId.toString(), content);
        }
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- same package services ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- called from SchedulerService ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    @Async
    void delete() {
        LocalDate baseDate = LocalDate.now().minusDays(Constants.NOTIFICATION_STORED_DURATION);
        
        notificationRepo.deleteAllInBatch(
            notificationRepo.findAll().stream().filter(e -> e.getCreatedDate().isBefore(baseDate)).collect(Collectors.toList())
        );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- called from SchedulerService ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
