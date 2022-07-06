package com.project.simplegw.document.approval.services;

import java.util.List;

import com.project.simplegw.document.approval.dtos.send.DtosApprover;
import com.project.simplegw.document.approval.entities.OngoingApproval;
import com.project.simplegw.document.approval.repositories.OngoingApprovalRepo;
import com.project.simplegw.document.approval.vos.Sign;
import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.system.security.LoginUser;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class OngoingApprovalService {
    private final OngoingApprovalRepo repo;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public OngoingApprovalService(OngoingApprovalRepo repo) {
        this.repo = repo;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재자 정보 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    OngoingApproval getOngoingApproval(Docs docs) {
        return repo.findByDocsId(docs.getId()).orElseGet(OngoingApproval::new);
    }

    boolean isCurrentApprover(Docs docs, LoginUser loginUser) {   // ApprovalDocsService에서 호출
        return loginUser.getMember().getId().equals( getOngoingApproval(docs).getApproverId() );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재자 정보 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 현재 결재자 등록 및 수정 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    void create(Docs docs, List<DtosApprover> approvers) throws Exception {   // ApproverService 에서 호출.
        repo.save( OngoingApproval.builder().docs(docs).ownerId( docs.getWriterId() ).build().update(approvers) );
    }

    void update(Docs docs, List<DtosApprover> approvers) throws Exception {   // ApproverService 에서 호출.
        OngoingApproval entity = getOngoingApproval(docs);

        if(approvers.stream().filter(e -> e.getSign() == Sign.PROCEED).findFirst().isPresent())
            repo.save( entity.update(approvers) );
        else
            repo.delete(entity);
    }

    void delete(Docs docs) throws Exception {   // ApproverService 에서 호출.
        repo.findByDocsId( docs.getId() ).ifPresent( repo::delete );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 현재 결재자 등록 및 수정 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //






    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- List search ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    List<OngoingApproval> getReceivedList(LoginUser loginUser) {
        return repo.findByApproverId( loginUser.getMember().getId() );
    }

    List<OngoingApproval> getProceedList(LoginUser loginUser) {
        return repo.findByOwnerId( loginUser.getMember().getId() );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- List search ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
