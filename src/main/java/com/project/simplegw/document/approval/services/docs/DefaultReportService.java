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
    private static final DocsType DEFAULT = DocsType.DEFAULT;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public DefaultReportService(ApprovalDocsService approvalDocsService, DocsService docsService, ApprovalConverter converter) {
        this.approvalDocsService = approvalDocsService;
        this.docsService = docsService;
        this.converter = converter;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }




    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg create(DtorDefaultReport dto, LoginUser loginUser) {
        DtorDocs dtorDocs = new DtorDocs().setTitle(dto.getTitle()).setContent(dto.getContent());
        return approvalDocsService.create(dtorDocs, DEFAULT, dto.getArrApproverId(), dto.getArrReferrerId(), loginUser);
    }

    public ServiceMsg update(Long docsId, DtorDefaultReport dto, LoginUser loginUser) {
        DtorDocs dtorDocs = new DtorDocs().setTitle(dto.getTitle()).setContent(dto.getContent());
        return approvalDocsService.update(docsId, dtorDocs, DEFAULT, dto.getArrApproverId(), dto.getArrReferrerId(), loginUser);
    }

    public ServiceMsg delete(Long docsId, LoginUser loginUser) {
        return approvalDocsService.delete(docsId, DEFAULT, loginUser);
    }



    public DtosApprovalDocs getDtosApprovalDocs(Long docsId, LoginUser loginUser) {
        DtosDocs dto = docsService.getDtosDocs(docsId, DEFAULT);
        return converter.getDtosApprovalDocs(dto).setLine(approvalDocsService.getDtosApprovalLinePack(docsId, DEFAULT, loginUser));
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg createTemp(DtorTempDefaultReport dto, LoginUser loginUser) {
        Long docsId = docsService.createTemp(converter.getDtorDocs(dto), DEFAULT, loginUser).getId();

        if(docsId == null)
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(DEFAULT.getTitle()).append(" 임시저장 에러입니다. 관리자에게 문의하세요.").toString() );
        
        else
            return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);
    }


    public ServiceMsg updateTemp(Long docsId, DtorTempDefaultReport dto, LoginUser loginUser) {
        TempDocs tempDocs = docsService.getTempDocsEntity(docsId, DEFAULT);

        if( ! docsService.isOwner(tempDocs, loginUser) )   // 수정은 본인만 가능.
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());

        docsService.updateTemp(docsId, converter.getDtorDocs(dto), DEFAULT);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }


    public ServiceMsg deleteTemp(Long docsId, LoginUser loginUser) {
        TempDocs tempDocs = docsService.getTempDocsEntity(docsId, DEFAULT);

        if( DEFAULT != tempDocs.getType() )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder("삭제 대상 문서가 ").append(DEFAULT.getTitle()).append("문서가 아닙니다.").toString() );


        if( docsService.isOwner(tempDocs, loginUser) ) {
            docsService.deleteTemp(tempDocs, loginUser);
            return new ServiceMsg().setResult(ServiceResult.SUCCESS);
            
        } else {
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());
        }
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
