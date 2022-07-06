package com.project.simplegw.document.approval.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.project.simplegw.document.approval.dtos.send.DtosApprovalDocsMin;
import com.project.simplegw.document.approval.dtos.send.DtosReferrer;
import com.project.simplegw.document.approval.entities.Referrer;
import com.project.simplegw.document.approval.helpers.ApprovalConverter;
import com.project.simplegw.document.approval.helpers.DtosApprovalDocsMinConverter;
import com.project.simplegw.document.approval.repositories.ReferrerRepo;
import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.member.services.MemberService;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ServiceMsg;
import com.project.simplegw.system.vos.ServiceResult;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class ReferrerService {
    private final ReferrerRepo repo;
    private final MemberService memberService;
    private final ApprovalCountService countService;
    private final ApprovalConverter converter;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public ReferrerService(ReferrerRepo repo, MemberService memberService, ApprovalCountService countService, ApprovalConverter converter) {
        this.repo = repo;
        this.memberService = memberService;
        this.countService = countService;
        this.converter = converter;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }




    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재문서의 참조자 등록 및 수정 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    ServiceMsg create(Docs docs, Long[] arrReferrerId, LoginUser loginUser) {
        if(arrReferrerId == null || arrReferrerId.length == 0)   // 참조자는 없는 경우도 있으므로
            return new ServiceMsg().setResult(ServiceResult.SUCCESS);

        try {
            List<Referrer> referrers = new ArrayList<>();

            List<Long> referrerIds = Arrays.asList(arrReferrerId);
            referrerIds.stream()
                .filter(e -> ! e.equals( loginUser.getMember().getId() ))   // 유저가 본인을 등록한 경우 제외한다.
                .forEach(e -> {
                    Referrer referrer = Referrer.builder().docs(docs).build().setReferrer( memberService.getMemberData(e) );
                    referrers.add(referrer);
                });

            repo.saveAll(referrers);

            referrerIds.forEach(e -> countService.removeReferrerDocsCntCache(e, true));   // 결재 참조받은 모든 멤버들의 참조 카운트 캐시 업데이트

            return new ServiceMsg().setResult(ServiceResult.SUCCESS);

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("save Exception.");
            log.warn("parameters: {}, {}", docs.toString(), arrReferrerId.toString());
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg("결재 참조자 저장 에러입니다. 관리자에게 문의하세요.");
        }
    }

    ServiceMsg update(Docs docs, Long[] arrReferrerId, LoginUser loginUser) {
        try {
            List<Referrer> referrers = repo.findByDocsIdOrderById(docs.getId());
            if(referrers != null)
                delete(docs, referrers);
            
            return create(docs, arrReferrerId, loginUser);

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("update Exception.");
            log.warn("parameters: {}, {}, user: {}", docs.toString(), arrReferrerId.toString(), loginUser.getMember().getId());
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg("결재 참조자 수정 에러입니다. 관리자에게 문의하세요.");
        }
    }

    private void delete(Docs docs, List<Referrer> referrers) throws Exception {
        repo.deleteAllInBatch(referrers);

        referrers.stream().mapToLong(Referrer::getMemberId).forEach(e -> countService.removeReferrerDocsCntCache(e, true));   // 결재 참조받은 모든 멤버들의 참조 카운트 캐시 제거
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재문서의 참조자 등록 및 수정 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재문서 view page에서 필요한 참조자 정보 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    List<DtosReferrer> getReferrers(Docs docs, LoginUser loginUser) {
        List<Referrer> referrers = repo.findByDocsIdOrderById(docs.getId());
        updateChecked(referrers, loginUser);

        return referrers.stream().map( e -> converter.getDtosReferrer(e).setChecked(e.getCheckedDatetime() != null) ).collect(Collectors.toList());
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재문서 view page에서 필요한 참조자 정보 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 참조자 확인 시간 업데이트 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    private void updateChecked(List<Referrer> referrers, LoginUser loginUser) {
        referrers.stream().filter(
            e -> e.getMemberId().equals(loginUser.getMember().getId()) && e.getCheckedDatetime() == null
        ).findFirst().ifPresent(e -> {
                countService.removeReferrerDocsCntCache(loginUser.getMember().getId(), false);
                repo.save(e.checked());
        });
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 참조자 확인 시간 업데이트 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- List search ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    List<Referrer> getUncheckedReferrers(LoginUser loginUser) {   // 확인하지 않은 문서 찾기
        return repo.findByMemberIdOrderById(loginUser.getMember().getId()).stream().filter(e -> e.getCheckedDatetime() == null).collect(Collectors.toList());
    }

    List<Referrer> getReferrers(LocalDate dateStart, LocalDate dateEnd, LoginUser loginUser) {
        return repo.findByMemberIdOrderById(loginUser.getMember().getId()).stream().collect(Collectors.toList());
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- List search ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 참조자로 받은 결재문서의 기간 검색 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    List<DtosApprovalDocsMin> getDocsForReferrer(DocsType type, LocalDate dateStart, LocalDate dateEnd, LoginUser loginUser) {
        List<Object[]> objList = repo.findForReferrer(loginUser.getMember().getId(), type, dateStart, dateEnd);
        return objList.stream().map( e -> DtosApprovalDocsMinConverter.fromObjs(e) ).collect(Collectors.toList());
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 참조자로 받은 결재문서의 기간 검색 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
