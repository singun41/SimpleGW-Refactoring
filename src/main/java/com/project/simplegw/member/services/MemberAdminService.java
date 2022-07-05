package com.project.simplegw.member.services;

import java.util.List;

import com.project.simplegw.member.dtos.admin.receive.DtorMemberCreate;
import com.project.simplegw.member.dtos.admin.receive.DtorMemberUpdate;
import com.project.simplegw.member.dtos.admin.receive.DtorPwForceUpdate;
import com.project.simplegw.member.dtos.admin.send.DtosMember;
import com.project.simplegw.member.dtos.admin.send.DtosMemberDetails;
import com.project.simplegw.system.vos.ServiceMsg;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class MemberAdminService {
    private final MemberService memberService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public MemberAdminService(MemberService memberService) {
        this.memberService = memberService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    public List<DtosMember> getMembers(boolean isRetired) {
        return memberService.getMembers(isRetired);
    }

    public DtosMemberDetails getMemberDetails(Long memberId) {
        return memberService.getMemberDetails(memberId);
    }

    public DtosMember getMember(Long memberId) {
        return memberService.getDtosMember(memberId);
    }

    public ServiceMsg create(DtorMemberCreate dto) {
        return memberService.create(dto);
    }

    public ServiceMsg update(Long memberId, DtorMemberUpdate dto) {
        return memberService.update(memberId, dto);
    }

    public ServiceMsg updateMemberPw(Long memberId, DtorPwForceUpdate dto) {
        return memberService.updateMemberPw(memberId, dto);
    }
}
