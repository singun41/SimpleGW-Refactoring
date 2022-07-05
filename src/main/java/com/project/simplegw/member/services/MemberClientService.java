package com.project.simplegw.member.services;

import com.project.simplegw.member.dtos.receive.DtorMyDetails;
import com.project.simplegw.member.dtos.receive.DtorPwChange;
import com.project.simplegw.member.dtos.send.DtosMyDetails;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ServiceMsg;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class MemberClientService {
    private final MemberService memberService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public MemberClientService(MemberService memberService) {
        this.memberService = memberService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }

    public boolean isOldPasswordMatched(String oldPw, LoginUser loginUser) {
        return memberService.isOldPasswordMatched(oldPw, loginUser);
    }

    public DtosMyDetails getMyDetails(LoginUser loginUser) {
        return memberService.getMyDetails(loginUser);
    }

    public ServiceMsg updateMyDetails(DtorMyDetails dto, LoginUser loginUser) {
        return memberService.updateMyDetails(dto, loginUser);
    }

    public ServiceMsg updateMyPassword(DtorPwChange dto, LoginUser loginUser) {
        return memberService.updateMyPassword(dto, loginUser);
    }
}
