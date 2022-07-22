package com.project.simplegw.document.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.project.simplegw.document.dtos.receive.DtorDocs;
import com.project.simplegw.document.dtos.send.DtosDocs;
import com.project.simplegw.document.dtos.send.DtosDocsMin;
import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.document.entities.TempDocs;
import com.project.simplegw.document.helpers.DocsConverter;
import com.project.simplegw.document.repositories.MeetingMinutesRepo;
import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.services.MenuAuthorityService;
import com.project.simplegw.system.vos.Menu;
import com.project.simplegw.system.vos.ResponseMsg;
import com.project.simplegw.system.vos.ServiceMsg;
import com.project.simplegw.system.vos.ServiceResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class MeetingMinutesService {
    private static final DocsType MEETING_MINUTES = DocsType.MEETING;

    private final MeetingMinutesRepo repo;
    private final DocsConverter converter;
    private final DocsService docsService;
    private final TempDocsService tempDocsService;
    private final MenuAuthorityService authService;
    

    public MeetingMinutesService(
        MeetingMinutesRepo repo, DocsConverter converter,
        DocsService docsService, TempDocsService tempDocsService, MenuAuthorityService authService
    ) {
        this.repo = repo;
        this.converter = converter;
        this.docsService = docsService;
        this.tempDocsService = tempDocsService;
        this.authService = authService;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }





    private List<DtosDocsMin> getDtosDocsMin(List<Docs> entities) {
        return entities.stream().map(converter::getDtosDocsMin).collect(Collectors.toList());
    }


    public List<DtosDocsMin> getList(LocalDate dateFrom, LocalDate dateTo, LoginUser loginUser) {
        return getDtosDocsMin( repo.findList(loginUser.getMember().getId(), MEETING_MINUTES, dateFrom, dateTo) );
    }





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg create(DtorDocs dto, LoginUser loginUser) {
        if( ! authService.isWritable(Menu.MEETING_MINUTES, loginUser) )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( ResponseMsg.UNAUTHORIZED.getTitle() );

        Long docsId = docsService.create(dto, MEETING_MINUTES, loginUser).getId();

        if(docsId == null)
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(MEETING_MINUTES.getTitle()).append(" 등록 에러입니다. 관리자에게 문의하세요.").toString() );
        
        else
            return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);
    }


    public ServiceMsg update(Long docsId, DtorDocs dto, LoginUser loginUser) {
        Docs docs = docsService.getDocsEntity(docsId, MEETING_MINUTES);

        if( ! authService.isUpdatable(Menu.MEETING_MINUTES, loginUser, docs.getWriterId()) )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());

        docsService.update(docsId, dto, MEETING_MINUTES);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }


    public ServiceMsg delete(Long docsId, LoginUser loginUser) {
        Docs docs = docsService.getDocsEntity(docsId, MEETING_MINUTES);

        if( ! authService.isDeletable(Menu.MEETING_MINUTES, loginUser, docs.getWriterId()) )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());

        if( MEETING_MINUTES != docs.getType() )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder("삭제 대상 문서가 ").append(MEETING_MINUTES.getTitle()).append("문서가 아닙니다.").toString() );

        docsService.delete(docs);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }



    public DtosDocs getDocs(Long docsId) {
        return docsService.getDtosDocs(docsId, MEETING_MINUTES);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public ServiceMsg createTemp(DtorDocs dto, LoginUser loginUser) {
        Long docsId = tempDocsService.create(dto, MEETING_MINUTES, loginUser).getId();

        if(docsId == null)
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(MEETING_MINUTES.getTitle()).append(" 임시저장 에러입니다. 관리자에게 문의하세요.").toString() );
        
        else
            return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);
    }
    

    public ServiceMsg updateTemp(Long docsId, DtorDocs dto, LoginUser loginUser) {
        TempDocs tempDocs = tempDocsService.getTempDocsEntity(docsId, MEETING_MINUTES);

        if( ! tempDocsService.isOwner(tempDocs, loginUser) )   // 임시저장 문서는 본인만 수정 가능.
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());

        tempDocsService.update(docsId, dto, MEETING_MINUTES);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }


    public ServiceMsg deleteTemp(Long docsId, LoginUser loginUser) {
        TempDocs tempDocs = tempDocsService.getTempDocsEntity(docsId, MEETING_MINUTES);

        if( ! tempDocsService.isOwner(tempDocs, loginUser) )   // 임시저장 문서는 본인만 삭제 가능.
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg(ResponseMsg.UNAUTHORIZED.getTitle());

        if( MEETING_MINUTES != tempDocs.getType() )
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder("삭제 대상 문서가 ").append(MEETING_MINUTES.getTitle()).append("문서가 아닙니다.").toString() );

        tempDocsService.delete(tempDocs, loginUser);
        return new ServiceMsg().setResult(ServiceResult.SUCCESS);
    }



    public DtosDocs getTempDocs(Long docsId) {
        return tempDocsService.getDtosDocsFromTempDocs(docsId, MEETING_MINUTES);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
