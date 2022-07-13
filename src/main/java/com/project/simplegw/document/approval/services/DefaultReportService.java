package com.project.simplegw.document.approval.services;

import com.project.simplegw.document.approval.dtos.receive.DtorDefaultReport;
import com.project.simplegw.document.approval.dtos.receive.DtorTempDefaultReport;
import com.project.simplegw.document.approval.dtos.send.DtosDefaultReport;
import com.project.simplegw.document.approval.helpers.ApprovalConverter;
import com.project.simplegw.document.dtos.receive.DtorDocs;
import com.project.simplegw.document.dtos.send.DtosDocs;
import com.project.simplegw.document.entities.TempDocs;
import com.project.simplegw.document.services.TempDocsService;
import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ResponseMsg;
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
public class DefaultReportService {
    private final ApprovalDocsService approvalDocsService;
    private final TempDocsService tempDocsService;
    private final ApprovalConverter converter;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public DefaultReportService(ApprovalDocsService approvalDocsService, TempDocsService tempDocsService, ApprovalConverter converter) {
        this.approvalDocsService = approvalDocsService;
        this.tempDocsService = tempDocsService;
        this.converter = converter;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }




    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg create(DocsType type, DtorDefaultReport dto, LoginUser loginUser) {
        DtorDocs dtorDocs = new DtorDocs().setTitle(dto.getTitle()).setContent(dto.getContent());
        return approvalDocsService.create(dtorDocs, type, dto.getArrApproverId(), dto.getArrReferrerId(), loginUser);
    }

    public ServiceMsg update(DocsType type, Long docsId, DtorDefaultReport dto, LoginUser loginUser) {
        DtorDocs dtorDocs = new DtorDocs().setTitle(dto.getTitle()).setContent(dto.getContent());
        return approvalDocsService.update(docsId, dtorDocs, type, dto.getArrApproverId(), dto.getArrReferrerId(), loginUser);
    }

    public ServiceMsg delete(DocsType type, Long docsId, LoginUser loginUser) {
        return approvalDocsService.delete(docsId, type, loginUser);
    }

    public DtosDefaultReport getDocs(DocsType type, Long docsId, LoginUser loginUser) {
        return (DtosDefaultReport) approvalDocsService.getDocs(docsId, type, loginUser);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg createTemp(DocsType type, DtorTempDefaultReport dto, LoginUser loginUser) {
        Long docsId = tempDocsService.create(converter.getDtorDocs(dto), type, loginUser).getId();

        if(docsId == null)
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(type.getTitle()).append(" 임시저장 에러입니다. 관리자에게 문의하세요.").toString() );
        
        else
            return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);
    }


    public ServiceMsg updateTemp(DocsType type, Long docsId, DtorTempDefaultReport dto, LoginUser loginUser) {
        TempDocs tempDocs = tempDocsService.getTempDocsEntity(docsId, type);

        if( ! tempDocsService.isOwner(tempDocs, loginUser) )   // 수정은 본인만 가능.
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());

        tempDocsService.update(docsId, converter.getDtorDocs(dto), type);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }


    public ServiceMsg deleteTemp(DocsType type, Long docsId, LoginUser loginUser) {
        TempDocs tempDocs = tempDocsService.getTempDocsEntity(docsId, type);

        if( type != tempDocs.getType() )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder("삭제 대상 문서가 ").append(type.getTitle()).append("문서가 아닙니다.").toString() );


        if( tempDocsService.isOwner(tempDocs, loginUser) ) {
            tempDocsService.delete(tempDocs, loginUser);
            return new ServiceMsg().setResult(ServiceResult.SUCCESS);
            
        } else {
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());
        }
    }


    public DtosDocs getTemp(DocsType docsType, Long docsId) {
        return tempDocsService.getDtosDocsFromTempDocs(docsId, docsType);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
