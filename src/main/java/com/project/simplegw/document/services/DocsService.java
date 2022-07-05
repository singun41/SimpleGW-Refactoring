package com.project.simplegw.document.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.project.simplegw.document.dtos.receive.DtorDocs;
import com.project.simplegw.document.dtos.receive.DtorDocsOptions;
import com.project.simplegw.document.dtos.send.DtosDocs;
import com.project.simplegw.document.dtos.send.DtosDocsMin;
import com.project.simplegw.document.dtos.send.DtosDocsOptions;
import com.project.simplegw.document.dtos.send.DtosTempDocs;
import com.project.simplegw.document.entities.Content;
import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.document.entities.DocsOptions;
import com.project.simplegw.document.entities.TempContent;
import com.project.simplegw.document.entities.TempDocs;
import com.project.simplegw.document.helpers.DocsConverter;
import com.project.simplegw.document.repositories.ContentRepo;
import com.project.simplegw.document.repositories.DocsOptionsRepo;
import com.project.simplegw.document.repositories.DocsRepo;
import com.project.simplegw.document.repositories.TempContentRepo;
import com.project.simplegw.document.repositories.TempDocsRepo;
import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.member.data.MemberData;
import com.project.simplegw.member.services.MemberService;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.Constants;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class DocsService {
    private final DocsRepo docsRepo;
    private final ContentRepo contentRepo;
    private final DocsConverter docsConverter;
    private final DocsOptionsRepo docsOptionsRepo;

    private final MemberService memberService;

    private final TempDocsRepo tempDocsRepo;
    private final TempContentRepo tempContentRepo;


    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public DocsService(
        DocsRepo docsRepo, ContentRepo contentRepo, DocsConverter docsConverter, DocsOptionsRepo docsOptionsRepo,
        MemberService memberService,
        TempDocsRepo tempDocsRepo, TempContentRepo tempContentRepo
    ) {
        this.docsRepo = docsRepo;
        this.contentRepo = contentRepo;
        this.docsConverter = docsConverter;
        this.docsOptionsRepo = docsOptionsRepo;
        
        this.memberService = memberService;

        this.tempDocsRepo = tempDocsRepo;
        this.tempContentRepo = tempContentRepo;
        
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public List<Docs> getRecentDocs20(DocsType type) {
        return docsRepo.findTop20ByTypeOrderByIdDesc(type);
    }
    


    private List<DtosDocsMin> getDtosDocsMin(List<Docs> entities) {
        return entities.stream().map(docsConverter::getDtosDocsMin).collect(Collectors.toList());
    }

    public List<DtosDocsMin> getDocs(DocsType type, LocalDate dateStart, LocalDate dateEnd) {
        return getDtosDocsMin( docsRepo.findByTypeAndCreatedDateBetweenOrderByIdDesc( type, dateStart, dateEnd ) );
    }

    private Docs getDocsEntity(Long docsId) {
        return docsRepo.findById(docsId).orElseGet(Docs::new);
    }
    public Docs getDocsEntity(Long docsId, DocsType type) {
        /*
            repo query method를 사용하지 않고 key 값으로 바로 찾은 뒤 type 체크해서 맞으면 엔티티 리턴 맞지 않으면 new 인스턴스를 리턴
            변경 이유:
                결재문서에서 getDtosDocs() 메서드 호출 후에 결재라인을 검색하기 위해서 다시 Docs 엔티티가 필요하여 호출하는데
                기존 query method를 이용하게 되면 where id = docs_id 쿼리 1개와 where id = docs_id and type = type 쿼리 1개 총 2개의 query 가 호출됨.
                
                그래서 아래 코드로 변경.
                getDtosDocs() 메서드에서 content를 이용해 docs를 가져오게 되면 id를 기준으로 가져오고
                아래 코드에서도 id를 기준으로 가져오기 때문에 DB에 query를 질의하기 전에 1차 캐시에 남아있는 엔티티를 바로 받을 수 있음.
        */
        Optional<Docs> target = docsRepo.findById(docsId);
        if(target.isPresent()) {
            Docs docs = target.get();
            if(docs.getType() == type)
                return docs;
            else
                return new Docs();
        } else {
            return new Docs();
        }
        // return docsRepo.findByIdAndType(docsId, type).orElseGet(Docs::new);
    }

    public DtosDocs getDtosDocs(Long docsId, DocsType type) {
        Docs docs;

        Content content = contentRepo.findByDocsId(docsId).orElseGet(Content::new);
        if(content.getId() == null)
            docs = new Docs();
        else
            docs = content.getDocs();
        
        if(docs.getType() != type)   // type이 맞지 않으면 비어있는 인스턴스로 대체.
            docs = new Docs();


        // repository의 네이티브 쿼리를 위 코드로 변경함.
        // Content content = contentRepo.findByDocsIdAndType(docsId, type).orElseGet(Content::new);
        // Docs docs = content.getDocs();

        return docsConverter.getDtosDocs( docs ).setContent(content.getContent());
    }

    public boolean isOwner(Docs docs, LoginUser loginUser) {
        return docs == null ? false : docs.getWriterId().equals(loginUser.getMember().getId());
    }



    public Docs create(DtorDocs dto, DocsType type, LoginUser loginUser) {
        MemberData memberData = memberService.getMemberData(loginUser);
        Docs docs =  Docs.builder().title(dto.getTitle()).type(type).build().setMemberData(memberData);

        try {
            Docs savedDocs = docsRepo.save(docs);
            Content contentEntity = Content.builder().content(dto.getContent()).build().bindDocs( savedDocs );

            contentRepo.save(contentEntity);
            return savedDocs;

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("createDocs exception.");
            log.warn("parameters: {}, {},  user: {}", dto.toString(), type.toString(), loginUser.getMember().getId());
            return null;
        }
    }

    public Docs update(Long docsId, DtorDocs dto, DocsType type) {
        Optional<Docs> targetDocs = docsRepo.findById(docsId);
        // Optional<Content> targetContent = contentRepo.findByDocsIdAndType(docsId, type);
        Optional<Content> targetContent = contentRepo.findByDocsId(docsId);

        try {
            if(targetDocs.isPresent()) {
                Docs docs = targetDocs.get();

                // type 체크
                if(docs.getType() != type) {
                    log.info("docs not exists.");
                    log.info("parameters: {}, {}, {}", docsId.toString(), dto.toString(), type.toString());
                    return null;
                }

                docs.updateTitle(dto.getTitle());
    
                Content content = targetContent.get();
                content.updateContent(dto.getContent());
    
                Docs savedDocs = docsRepo.save(docs);
                contentRepo.save( content.bindDocs(savedDocs) );
    
                return savedDocs;
    
            } else {
                log.info("docs not exists.");
                log.info("parameters: {}, {}, {}", docsId.toString(), dto.toString(), type.toString());
                return null;
            }

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("updateDocs exception.");
            log.warn("parameters: {}, {}, {}", docsId.toString(), dto.toString(), type.toString());
            return null;
        }
    }

    public boolean delete(Docs docs) {
        if(docs == null || docs.getId() == null)
            return false;

        try {
            docsRepo.delete(docs);
            return true;

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("deleteDocs exception.");
            log.warn("parameters: {}", docs.toString());
            
            return false;
        }
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs options ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    List<DocsOptions> getOptions() {
        return docsOptionsRepo.findAll();
    }

    List<DtosDocsOptions> getOptionsDto() {
        return docsOptionsRepo.findAll().stream().map(docsConverter::getDtosDocsOptions).collect(Collectors.toList());
    }

    DtosDocsOptions getOptions(Long docsId) {
        return docsConverter.getDtosDocsOptions( docsOptionsRepo.findByDocsId(docsId).orElseGet(DocsOptions::new) );
    }

    public void updateOptions(Long docsId, DtorDocsOptions dto) {
        if(docsId == null)
            return;

        Optional<DocsOptions> target = docsOptionsRepo.findByDocsId(docsId);
        log.info(dto.toString());
        if(target.isPresent()) {
            DocsOptions options = target.get();
            
            if(dto.isUse())
                docsOptionsRepo.save( options.updateOptions(dto) );
            else
                docsOptionsRepo.delete(options);
        
        } else {
            Docs docs = getDocsEntity(docsId);
            if(docs.getId() == null)
                return;

            if(dto.isUse())
                docsOptionsRepo.save( DocsOptions.builder().build().updateOptions(dto).bindDocs(docs) );
        }
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs options ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    private List<DtosTempDocs> getDtosTempDocs(List<TempDocs> entities) {
        return entities.stream().map( e -> docsConverter.getDtosTempDocs(e).setTypeTitle(e.getType().getTitle()).updateGroup() ).collect(Collectors.toList());
    }

    public List<DtosTempDocs> getTempDocs(LoginUser loginUser) {
        return getDtosTempDocs( tempDocsRepo.findByWriterId( loginUser.getMember().getId() ) );
    }
    
    public TempDocs getTempDocsEntity(Long docsId, DocsType type) {
        return tempDocsRepo.findByIdAndType(docsId, type).orElseGet(TempDocs::new);
    }

    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_TEMPDOCS_COUNT, key = "#loginUser.getMember().getId()")
    public long getTempDocsCount(LoginUser loginUser) {
        return tempDocsRepo.countByWriterId( loginUser.getMember().getId() );
    }

    public DtosDocs getDtosDocsFromTempDocs(Long docsId, DocsType type) {
        TempContent content = tempContentRepo.findByTempDocsId(docsId).orElseGet(TempContent::new);
        TempDocs docs = content.getTempDocs();
        return docsConverter.getDtosDocs( docs ).setContent( content.getContent() );
    }

    public boolean isOwner(TempDocs tempDocs, LoginUser loginUser) {
        return tempDocs == null ? false : tempDocs.getWriterId().equals( loginUser.getMember().getId() );
    }


    @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_TEMPDOCS_COUNT, allEntries = false, key = "#loginUser.getMember().getId()")
    public TempDocs createTemp(DtorDocs dto, DocsType type, LoginUser loginUser) {
        TempDocs tempDocs =  TempDocs.builder().title(dto.getTitle()).type(type).build().setWriterId(loginUser.getMember());

        try {
            TempDocs savedTempDocs = tempDocsRepo.save(tempDocs);
            TempContent contentEntity = TempContent.builder().content(dto.getContent()).build().bindTempDocs( savedTempDocs );

            tempContentRepo.save(contentEntity);
            return savedTempDocs;

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("createDocs exception.");
            log.warn("parameters: {}, {}, user: {}", dto.toString(), type.toString(), loginUser.getMember().getId());
            log.warn("Docs value: {}", tempDocs.toString());
            return null;
        }
    }

    public TempDocs updateTemp(Long docsId, DtorDocs dto, DocsType type) {
        Optional<TempDocs> targetDocs = tempDocsRepo.findById(docsId);
        Optional<TempContent> targetContent = tempContentRepo.findByTempDocsId(docsId);

        try {
            if(targetDocs.isPresent()) {
                TempDocs tempDocs = targetDocs.get();
                tempDocs.updateTitle(dto.getTitle());
    
                TempContent tempContent = targetContent.get();
                tempContent.updateContent(dto.getContent());
    
                TempDocs tempSavedDocs = tempDocsRepo.save(tempDocs);
                tempContentRepo.save( tempContent.bindTempDocs(tempSavedDocs) );
    
                return tempSavedDocs;
    
            } else {
                log.info("docs not exists.");
                log.warn("parameters: {}, {}", dto.toString(), type.toString());
                return null;
            }

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("updateDocs exception.");
            log.warn("parameters: {}, {}", dto.toString(), type.toString());
            return null;
        }
    }

    @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_TEMPDOCS_COUNT, allEntries = false, key = "#loginUser.getMember().getId()")
    public void deleteTemp(TempDocs tempDocs, LoginUser loginUser) {
        if(tempDocs == null || tempDocs.getId() == null)
            return;

        try {
            tempDocsRepo.delete(tempDocs);

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("deleteDocs exception.");
            log.warn("parameters: {}", tempDocs.toString());
        }
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- temp docs ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
