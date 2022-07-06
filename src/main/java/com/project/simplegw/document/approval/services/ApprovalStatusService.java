package com.project.simplegw.document.approval.services;

import java.util.List;

import com.project.simplegw.document.approval.dtos.send.DtosApprover;
import com.project.simplegw.document.approval.entities.ApprovalStatus;
import com.project.simplegw.document.approval.repositories.ApprovalStatusRepo;
import com.project.simplegw.document.entities.Docs;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class ApprovalStatusService {
    private final ApprovalStatusRepo repo;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public ApprovalStatusService(ApprovalStatusRepo repo) {
        this.repo = repo;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재문서의 결재 상태 조회 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    ApprovalStatus getStatus(Docs docs) {
        return repo.findByDocsId(docs.getId()).orElseGet(ApprovalStatus::new);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재문서의 결재 상태 조회 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재문서의 결재 상태 등록 및 수정 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    void create(Docs docs, List<DtosApprover> approvers) throws Exception {   // ApproverService 에서 호출.
        repo.save( ApprovalStatus.builder().build().bindDocs(docs).init(approvers) );
    }

    void update(Docs docs, List<DtosApprover> approvers) throws Exception {   // ApproverService 에서 호출.
        repo.save( getStatus(docs).update(approvers) );
    }

    void delete(Docs docs) throws Exception {   // ApproverService 에서 호출.
        repo.delete( getStatus(docs) );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- 결재문서의 결재 상태 등록 및 수정 ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
