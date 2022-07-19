package com.project.simplegw.document.approval.services.details;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.project.simplegw.code.services.BasecodeService;
import com.project.simplegw.document.approval.dtos.receive.details.dayoff.DtorDayoff;
import com.project.simplegw.document.approval.dtos.send.details.dayoff.DtosDayoff;
import com.project.simplegw.document.approval.dtos.send.details.dayoff.DtosDayoffDetails;
import com.project.simplegw.document.approval.entities.details.Dayoff;
import com.project.simplegw.document.approval.helpers.details.DayoffConverter;
import com.project.simplegw.document.approval.repositories.details.DayoffRepo;
import com.project.simplegw.document.approval.services.ApprovalDocsService;
import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.ServiceMsg;
import com.project.simplegw.system.vos.ServiceResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class DayoffService {
    private final DayoffRepo repo;
    private final DayoffConverter converter;
    private final ApprovalDocsService approvalDocsService;
    private final BasecodeService basecodeService;

    private final DocsType DAYOFF = DocsType.DAYOFF;

    public DayoffService(DayoffRepo repo, DayoffConverter converter, ApprovalDocsService approvalDocsService, BasecodeService basecodeService) {
        this.repo = repo;
        this.converter = converter;
        this.approvalDocsService = approvalDocsService;
        this.basecodeService = basecodeService;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    private List<Dayoff> createEntities(DtorDayoff dto, Docs docs) {
        return dto.getDetails().stream().map(e -> converter.getEntity(e).bindDocs(docs).updateSeq(dto.getDetails().indexOf(e)).updateDuration()).collect(Collectors.toList());
    }
    

    
    public ServiceMsg create(DtorDayoff dto, LoginUser loginUser) {
        ServiceMsg result = approvalDocsService.create(converter.getDtorDocs(dto), DAYOFF, dto.getArrApproverId(), dto.getArrReferrerId(), loginUser);
        
        if(result.getResult() == ServiceResult.SUCCESS) {
            Long docsId = (Long) result.getReturnObj();
            Docs docs = approvalDocsService.getDocsEntity(docsId, DAYOFF);

            try {
                repo.saveAll( createEntities(dto, docs) );
                return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);

            } catch(Exception e) {
                e.printStackTrace();
                log.warn("create dayoff details exception.");
                log.warn("parameters: {}, user: {}", dto.toString(), loginUser.getMember().getId());

                return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(DAYOFF.getTitle()).append(" 디테일 등록 에러입니다. 관리자에게 문의하세요.").toString() );
            }

        } else {
            return result;
        }
    }


    public ServiceMsg update(Long docsId, DtorDayoff dto, LoginUser loginUser) {
        ServiceMsg result = approvalDocsService.update(docsId, converter.getDtorDocs(dto), DAYOFF, dto.getArrApproverId(), dto.getArrReferrerId(), loginUser);

        if(result.getResult() == ServiceResult.SUCCESS) {
            try {
                List<Dayoff> oldEntities = repo.findByDocsId(docsId);
                repo.deleteAllInBatch(oldEntities);
                repo.saveAll( createEntities(dto, approvalDocsService.getDocsEntity(docsId, DAYOFF)) );

                return new ServiceMsg().setResult(ServiceResult.SUCCESS).setReturnObj(docsId);

            } catch(Exception e) {
                e.printStackTrace();
                log.warn("update dayoff details exception.");
                log.warn("parameters: {}, user: {}", dto.toString(), loginUser.getMember().getId());

                return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg( new StringBuilder(DAYOFF.getTitle()).append(" 디테일 수정 에러입니다. 관리자에게 문의하세요.").toString() );
            }
        } else {
            return result;
        }
    }
    
    
    
    // docs가 삭제되면 cascade로 dayoff entity들은 자동 삭제되므로 delete 메서드는 필요하지 않음.


    public DtosDayoff getDocs(Long docsId, LoginUser loginUser) {
        DtosDayoff docs = converter.getDocs( approvalDocsService.getDocs(docsId, DAYOFF, loginUser) );
        return docs.setDetails(getDetails(docsId));
    }

    public List<DtosDayoffDetails> getDetails(Long docsId) {
        return repo.findByDocsId(docsId).stream().map(
            e -> converter.getDetails(e).setValue(
                basecodeService.getDayoffCodes().stream().filter( code -> e.getCode().equals(code.getKey()) ).findFirst().get().getValue()
            )
        ).collect(Collectors.toList());
    }



    
    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //

    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
