package com.project.simplegw.system.services;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemScheduler {

    // cron expression: s m h d M mon-fri y
    // mon-fri --> 0:sun ~ 5:fri, 6:sat, 7:sun (0, 7 둘 다 sunday)
    // year는 생략 가능
    // * --> all
    // ? --> 어떤값이든 관계없음, 날짜와 요일만 가능
    // - --> 범위
    // , --> 여러 값
    // L --> 마지막 값, 날짜와 요일만 가능
    // W --> 월~금 또는 가장 가까운 월, 금을 설정
    // # --> 몇번 째 특정 요일 지정
    
    // samples
    // 0 0/5 * * * --> 매 5, 10, 15분... 5분 단위로 실행
    // 0 0 13 * * * --> 매일 13시마다 실행
    
    private static final String FIRST_DAY_OF_EVERY_MONTS = "0 0 0 1/1 * *";
    private static final String EVERY_MIDNIGHTS = "0 0 0 * * *";
    private static final String EVERY_HOURS = "0 0 0/1 * * *";
    private static final String EVERY_30_MINS = "0 0/30 * * * *";
    private static final String EVERY_30_SECS = "0/30 * * * * *";
    private static final String EVERY_15_SECS = "0/15 * * * * *";


    private final SseService sseService;
    private final CacheService cacheService;
    private final NotificationService notiService;


    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public SystemScheduler(SseService sseService, CacheService cacheService, NotificationService notiService) {
        this.sseService = sseService;
        this.cacheService = cacheService;
        this.notiService = notiService;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }

    @Scheduled(cron = EVERY_30_MINS)
    private void sseMonitoring() {
        sseService.monitoring();
    }




    // @Scheduled(cron = EVERY_15_SECS)
    // private void cacheMonitoring() {
    //     cacheService.monitoring();
    // }



    
    @Scheduled(cron = EVERY_MIDNIGHTS)
    private void removeHolidays() {
        log.info("execute removeHolidays()");
        cacheService.removeHolidays();
    }

    @Scheduled(cron = EVERY_MIDNIGHTS)
    private void removeAlarms() {
        log.info("execute removeAlarms");
        cacheService.removeAlarms();
    }

    @Scheduled(cron = EVERY_MIDNIGHTS)
    private void removeOldNotifications() {
        log.info("execute removeOldNotifications");
        notiService.removeOldNotifications();
    }




    @Scheduled(cron = FIRST_DAY_OF_EVERY_MONTS)
    private void removeTempdocsCount() {
        log.info("execute removeTempdocsCount()");
        cacheService.removeTempdocsCount();
    }
}
