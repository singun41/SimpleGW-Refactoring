package com.project.simplegw.document.approval.services.docs;

import com.project.simplegw.document.approval.dtos.receive.docs.DtorDefaultReport;
import com.project.simplegw.document.approval.dtos.receive.docs.DtorTempDefaultReport;
import com.project.simplegw.document.approval.dtos.send.DtosApprovalDocs;
import com.project.simplegw.document.approval.helpers.ApprovalConverter;
import com.project.simplegw.document.approval.services.ApprovalDocsService;
import com.project.simplegw.document.dtos.receive.DtorDocs;
import com.project.simplegw.document.dtos.send.DtosDocs;
import com.project.simplegw.document.entities.TempDocs;
import com.project.simplegw.document.services.DocsService;
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
    private final DocsService docsService;
    private final ApprovalConverter converter;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public DefaultReportService(ApprovalDocsService approvalDocsService, DocsService docsService, ApprovalConverter converter) {
        this.approvalDocsService = approvalDocsService;
        this.docsService = docsService;
        this.converter = converter;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }




    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg create(DocsType docsType, DtorDefaultReport dto, LoginUser loginUser) {
        DtorDocs dtorDocs = new DtorDocs().setTitle(dto.getTitle()).setContent(dto.getContent());
        return approvalDocsService.create(dtorDocs, docsType, dto.getArrApproverId(), dto.getArrReferrerId(), loginUser);
    }

    public ServiceMsg update(DocsType docsType, Long docsId, DtorDefaultReport dto, LoginUser loginUser) {
        DtorDocs dtorDocs = new DtorDocs().setTitle(dto.getTitle()).setContent(dto.getContent());
        return approvalDocsService.update(docsId, dtorDocs, docsType, dto.getArrApproverId(), dto.getArrReferrerId(), loginUser);
    }

    public ServiceMsg delete(DocsType docsType, Long docsId, LoginUser loginUser) {
        return approvalDocsService.delete(docsId, docsType, loginUser);
    }

    public DtosApprovalDocs getDtosApprovalDocs(DocsType docsType, Long docsId, LoginUser loginUser) {
        return converter.getDtosApprovalDocs( docsService.getDtosDocs(docsId, docsType) ).setLine( approvalDocsService.getDtosApprovalLinePack(docsId, docsType, loginUser) );
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg createTemp(DocsType docsType, DtorTempDefaultReport dto, LoginUser loginUser) {
        Long docsId = docsService.createTemp(converter.getDtorDocs(dto), docsType, loginUser).getId();

        if(docsId == null)
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(docsType.getTitle()).append(" 임시저장 에러입니다. 관리자에게 문의하세요.").toString() );
        
        else
            return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);
    }


    public ServiceMsg updateTemp(DocsType docsType, Long docsId, DtorTempDefaultReport dto, LoginUser loginUser) {
        TempDocs tempDocs = docsService.getTempDocsEntity(docsId, docsType);

        if( ! docsService.isOwner(tempDocs, loginUser) )   // 수정은 본인만 가능.
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());

        docsService.updateTemp(docsId, converter.getDtorDocs(dto), docsType);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }


    public ServiceMsg deleteTemp(DocsType docsType, Long docsId, LoginUser loginUser) {
        TempDocs tempDocs = docsService.getTempDocsEntity(docsId, docsType);

        if( docsType != tempDocs.getType() )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder("삭제 대상 문서가 ").append(docsType.getTitle()).append("문서가 아닙니다.").toString() );


        if( docsService.isOwner(tempDocs, loginUser) ) {
            docsService.deleteTemp(tempDocs, loginUser);
            return new ServiceMsg().setResult(ServiceResult.SUCCESS);
            
        } else {
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());
        }
    }


    public DtosDocs getTemp(DocsType docsType, Long docsId) {
        return docsService.getDtosDocsFromTempDocs(docsId, docsType);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
