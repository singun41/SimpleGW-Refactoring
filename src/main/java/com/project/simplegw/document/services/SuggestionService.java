package com.project.simplegw.document.services;

import java.time.LocalDate;
import java.util.List;

import com.project.simplegw.document.dtos.receive.DtorDocs;
import com.project.simplegw.document.dtos.send.DtosDocs;
import com.project.simplegw.document.dtos.send.DtosDocsMin;
import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.document.entities.TempDocs;
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
public class SuggestionService {
    private final DocsService docsService;
    private final TempDocsService tempDocsService;
    private static final DocsType SUGGESTION = DocsType.SUGGESTION;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public SuggestionService(DocsService docsService, TempDocsService tempDocsService) {
        this.docsService = docsService;
        this.tempDocsService = tempDocsService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }




    public List<DtosDocsMin> getList(LocalDate dateStart, LocalDate dateEnd) {
        return docsService.getDocs(DocsType.SUGGESTION, dateStart, dateEnd);
    }



    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg create(DtorDocs dto, LoginUser loginUser) {
        Long docsId = docsService.create(dto, SUGGESTION, loginUser).getId();

        if(docsId == null)
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(SUGGESTION.getTitle()).append(" 등록 에러입니다. 관리자에게 문의하세요.").toString() );
        
        else
            return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);
    }


    public ServiceMsg update(Long docsId, DtorDocs dto, LoginUser loginUser) {
        Docs docs = docsService.getDocsEntity(docsId, SUGGESTION);

        if( ! docsService.isOwner(docs, loginUser) )   // 수정은 본인만 가능.
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());

        docsService.update(docsId, dto, SUGGESTION);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }


    public ServiceMsg delete(Long docsId, LoginUser loginUser) {
        Docs docs = docsService.getDocsEntity(docsId, SUGGESTION);

        if( SUGGESTION != docs.getType() )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder("삭제 대상 문서가 ").append(SUGGESTION.getTitle()).append("문서가 아닙니다.").toString() );


        if( docsService.isOwner(docs, loginUser) ) {
            docsService.delete(docs);
            return new ServiceMsg().setResult(ServiceResult.SUCCESS);
            
        } else {
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());
        }
    }



    public DtosDocs getSuggestion(Long docsId) {
        return docsService.getDtosDocs(docsId, SUGGESTION);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg createTemp(DtorDocs dto, LoginUser loginUser) {
        Long docsId = tempDocsService.create(dto, SUGGESTION, loginUser).getId();

        if(docsId == null)
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(SUGGESTION.getTitle()).append(" 등록 에러입니다. 관리자에게 문의하세요.").toString() );
        
        else
            return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);
    }


    public ServiceMsg updateTemp(Long docsId, DtorDocs dto, LoginUser loginUser) {
        TempDocs tempDocs = tempDocsService.getTempDocsEntity(docsId, SUGGESTION);

        if( ! tempDocsService.isOwner(tempDocs, loginUser) )   // 수정은 본인만 가능.
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());

        tempDocsService.update(docsId, dto, SUGGESTION);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }


    public ServiceMsg deleteTemp(Long docsId, LoginUser loginUser) {
        TempDocs tempDocs = tempDocsService.getTempDocsEntity(docsId, SUGGESTION);

        if( SUGGESTION != tempDocs.getType() )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder("삭제 대상 문서가 ").append(SUGGESTION.getTitle()).append("문서가 아닙니다.").toString() );


        if( tempDocsService.isOwner(tempDocs, loginUser) ) {
            tempDocsService.delete(tempDocs, loginUser);
            return new ServiceMsg().setResult(ServiceResult.SUCCESS);
            
        } else {
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());
        }
    }



    public DtosDocs getTempSuggestion(Long docsId) {
        return tempDocsService.getDtosDocsFromTempDocs(docsId, SUGGESTION);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
