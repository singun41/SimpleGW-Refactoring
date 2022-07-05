package com.project.simplegw.document.services;

import java.time.LocalDate;
import java.util.List;

import com.project.simplegw.document.dtos.receive.DtorDocs;
import com.project.simplegw.document.dtos.send.DtosDocs;
import com.project.simplegw.document.dtos.send.DtosDocsMin;
import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.document.vos.DocsType;
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
public class ArchiveService {
    private final DocsService docsService;
    private static final DocsType ARCHIVE = DocsType.ARCHIVE;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public ArchiveService(DocsService docsService) {
        this.docsService = docsService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }


    public List<DtosDocsMin> getList(LocalDate dateStart, LocalDate dateEnd) {
        return docsService.getDocs(ARCHIVE, dateStart, dateEnd);
    }



    // Archive(자료실)은 임시저장 기능을 제공하지 않는다.


    
    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg create(DtorDocs dto, LoginUser loginUser) {
        Long docsId = docsService.create(dto, ARCHIVE, loginUser).getId();

        if(docsId == null)
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(ARCHIVE.getTitle()).append(" 등록 에러입니다. 관리자에게 문의하세요.").toString() );
        
        else
            return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);
    }


    public ServiceMsg update(Long docsId, DtorDocs dto, LoginUser loginUser) {
        docsService.update(docsId, dto, ARCHIVE);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }


    public ServiceMsg delete(Long docsId) {
        Docs docs = docsService.getDocsEntity(docsId, ARCHIVE);

        if( ARCHIVE != docs.getType() )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder("삭제 대상 문서가 ").append(ARCHIVE.getTitle()).append("문서가 아닙니다.").toString() );


        docsService.delete(docs);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }



    public DtosDocs getArchive(Long docsId) {
        return docsService.getDtosDocs(docsId, ARCHIVE);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
