package com.project.simplegw.code.services;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.project.simplegw.code.dtos.receive.DtorBasecode;
import com.project.simplegw.code.dtos.send.DtosBasecode;
import com.project.simplegw.code.dtos.send.DtosCodeValue;
import com.project.simplegw.code.entities.Basecode;
import com.project.simplegw.code.helper.BasecodeConverter;
import com.project.simplegw.code.repositories.BasecodeRepo;
import com.project.simplegw.code.vos.BasecodeType;
import com.project.simplegw.system.vos.Constants;
import com.project.simplegw.system.vos.ServiceMsg;
import com.project.simplegw.system.vos.ServiceResult;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class BasecodeService {
    private final BasecodeRepo repo;
    private final BasecodeConverter converter;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public BasecodeService(BasecodeRepo repo, BasecodeConverter converter) {
        this.repo = repo;
        this.converter = converter;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }


    private Stream<Basecode> getCodeStream(BasecodeType type) {
        return repo.findByType(type).stream().sorted(Comparator.comparing(Basecode::getSeq));
    }



    public List<BasecodeType> getAllTypes() {
        return Arrays.stream(BasecodeType.values()).sorted(Comparator.comparing(BasecodeType::getTitle)).toList();   // 불변 List를 리턴해도 된다.
    }

    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_BASECODE, key = "#type.name()")
    public List<DtosBasecode> getCodes(BasecodeType type) {
        return getCodeStream(type).map(converter::getDtosBasecode).collect(Collectors.toList());
    }

    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_BASECODE, key = "#id")
    public DtosBasecode getCode(Long id) {
        return converter.getDtosBasecode( repo.findById(id).get() );
    }



    private boolean isDuplicated(BasecodeType type, String code) {
        return getCodeStream(type).filter(e -> e.getCode().equals(code)).findAny().isPresent();
    }

    @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = {Constants.CACHE_BASECODE, Constants.CACHE_JOB_TITLES, Constants.CACHE_DAYOFF_CODES}, allEntries = true)
    public ServiceMsg create(BasecodeType type, DtorBasecode dto) {
        try {
            if(isDuplicated(type, dto.getCode())) {
                return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg("중복된 코드입니다.");
            } else {
                Basecode entity = converter.getBasecode(dto);
                repo.save( entity.setType(type) );
    
                return new ServiceMsg().setResult(ServiceResult.SUCCESS);
            }

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("create exception");
            log.warn("parameters: {}, {}", type.toString(), dto.toString());
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg("기초코드 등록 에러입니다. 로그를 확인하세요.");
        }
    }

    @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = {Constants.CACHE_BASECODE, Constants.CACHE_JOB_TITLES, Constants.CACHE_DAYOFF_CODES}, allEntries = true)
    public ServiceMsg update(Long id, DtorBasecode dto) {
        try {
            Optional<Basecode> findCode = repo.findById(id);
            if(findCode.isPresent()) {
                Basecode entity = findCode.get();
    
                entity.updateValue(dto.getValue()).updateSeq(dto.getSeq()).updateRemarks(dto.getRemarks());
                if(dto.isEnabled())
                    entity.setEnabled();
                else
                    entity.setDisabled();
                
                repo.save(entity);
                return new ServiceMsg().setResult(ServiceResult.SUCCESS);
            
            } else {
                return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg("코드 데이터가 없습니다.");
            }

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("update exception");
            log.warn("parameters: {}, {}", id.toString(), dto.toString());
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg("기초코드 업데이트 에러입니다. 로그를 확인하세요.");
        }
    }


    
    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_JOB_TITLES)
    public List<String> getJobTitles() {
        return getCodeStream(BasecodeType.JOB_TITLE).map(Basecode::getValue).collect(Collectors.toList());
    }

    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_DAYOFF_CODES)
    public List<DtosCodeValue> getDayoffCodes() {
        return getCodeStream(BasecodeType.DAYOFF).map(converter::getDtosCodeValue).collect(Collectors.toList());
    }
}
