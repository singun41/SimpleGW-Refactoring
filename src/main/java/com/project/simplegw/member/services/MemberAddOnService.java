package com.project.simplegw.member.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.project.simplegw.document.approval.repositories.details.dayoff.DayoffRepo;
import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.member.dtos.send.DtosDayoffCnt;
import com.project.simplegw.member.entities.MemberAddOn;
import com.project.simplegw.member.helpers.MemberAddOnConverter;
import com.project.simplegw.member.repositories.MemberAddOnRepo;
import com.project.simplegw.system.security.LoginUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class MemberAddOnService {
    private final MemberAddOnRepo repo;
    private final MemberAddOnConverter converter;
    private final DayoffRepo dayoffRepo;   // 순환참조 문제가 있어 service 대신 repository 를 직접 가져온다.
    

    public MemberAddOnService(MemberAddOnRepo repo, MemberAddOnConverter converter, DayoffRepo dayoffRepo) {
        this.repo = repo;
        this.converter = converter;
        this.dayoffRepo = dayoffRepo;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }


    public DtosDayoffCnt getDayoffCount(LoginUser loginUser) {
        return converter.getDayoffCnt(repo.findByMemberId(loginUser.getMember().getId()).orElseGet(MemberAddOn::new));
    }


    @Async
    public void updateMemberDayoffCount(Docs docs) {
        repo.findByMemberId(docs.getWriterId()).ifPresent(member -> member.updateDayoffUseCnt( dayoffRepo.findByDocsId(docs.getId()) ));
    }
}
