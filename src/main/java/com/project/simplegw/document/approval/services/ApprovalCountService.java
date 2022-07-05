package com.project.simplegw.document.approval.services;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.project.simplegw.document.approval.repositories.OngoingApprovalRepo;
import com.project.simplegw.document.approval.repositories.ReferrerRepo;
import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.services.SseApprovalService;
import com.project.simplegw.system.vos.Constants;
import com.project.simplegw.system.vos.SseApprovalType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class ApprovalCountService {   // 결재 진행, 결재 요청, 결재 참조 카운트를 캐싱하기 위한 서비스
    private final SseApprovalService sseService;
    private final OngoingApprovalRepo ongoingApprovalRepo;
    private final ReferrerRepo referrerRepo;


    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public ApprovalCountService(SseApprovalService sseService, OngoingApprovalRepo ongoingApprovalRepo, ReferrerRepo referrerRepo) {
        this.sseService = sseService;
        this.ongoingApprovalRepo = ongoingApprovalRepo;
        this.referrerRepo = referrerRepo;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }
    


    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_PROCEED_COUNT, key = "#loginUser.getMember().getId()")
    public long getProceedDocsCnt(LoginUser loginUser) {   // 진행중인 결재문서 카운트 캐싱, ApprovalDocsController에서 호출
        return ongoingApprovalRepo.countByOwnerId( loginUser.getMember().getId() );
    }


    @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_PROCEED_COUNT, allEntries = false, key = "#loginUser.getMember().getId()")
    public void removeProceedDocsCntCache(LoginUser loginUser) { }   // ApprovalDocsService.create()에서 호출.


    @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_PROCEED_COUNT, allEntries = false, key = "#docs.getWriterId()")
    public void removeProceedDocsCntCache(SseApprovalType type, Docs docs) {   // ApproverService.confirmed(), rejected()에서 호출.
        sseService.sendToApprovalSubmitter(type, docs);
    }




    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_APPROVER_COUNT, key = "#loginUser.getMember().getId()")
    public long getApproverDocsCnt(LoginUser loginUser) {   // 결재 요청받은 문서 카운트 캐싱, ApprovalDocsController에서 호출
        return ongoingApprovalRepo.countByApproverId( loginUser.getMember().getId() );
    }

    @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_APPROVER_COUNT, allEntries = false, key = "#memberId")
    public void removeApproverDocsCntCache(Long memberId) {   // ApproverService.create(), delete()에서 호출.
        sseService.sendToCurrentApprover(memberId);
    }




    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_REFERRER_COUNT, key = "#loginUser.getMember().getId()")
    public long getReferrerDocsCnt(LoginUser loginUser) {   // 결재 참조받은 문서 카운트 캐싱, ApprovalDocsController에서 호출
        return referrerRepo.countByMemberIdAndCheckedDatetimeIsNull( loginUser.getMember().getId() );
    }

    @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_REFERRER_COUNT, allEntries = false, key = "#memberId")
    public void removeReferrerDocsCntCache(Long memberId) {   // ReferrerService.create(), delete()에서 호출.
        sseService.sendToReferrer(memberId);
    }



    @Caching(evict = {
        @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_PROCEED_COUNT, allEntries = false, key = "#loginUser.getMember().getId()"),
        @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_APPROVER_COUNT, allEntries = true),
        @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_APPROVAL_REFERRER_COUNT, allEntries = true)
    })
    public void removeAllApprovalCache(LoginUser loginUser) {   // ApprovalDocsService.delete()에서 호출.
        log.info("removeAllApprovalCache() method called. caller: ApprovalDocsService.delete(), user: {}", loginUser.getMember().getId());
    }
}
