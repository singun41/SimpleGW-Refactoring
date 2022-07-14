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
        
        initialization();

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- initialization ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    private void initialization() {
        setJobTitle();
        setDayoff();
        setOvertime();
    }

    private void setJobTitle() {
        if(getCodeStream(BasecodeType.JOB_TITLE).count() > 0)
            return;
        
        DtorBasecode A10 = new DtorBasecode().setCode("A10").setValue("회장").setSeq(1).setEnabled(true);
        DtorBasecode A20 = new DtorBasecode().setCode("A20").setValue("부회장").setSeq(1).setEnabled(true);

        DtorBasecode B10 = new DtorBasecode().setCode("B10").setValue("사장").setSeq(1).setEnabled(true);
        DtorBasecode B20 = new DtorBasecode().setCode("B20").setValue("부사장").setSeq(1).setEnabled(true);

        DtorBasecode C10 = new DtorBasecode().setCode("C10").setValue("전무").setSeq(1).setEnabled(true);
        DtorBasecode C20 = new DtorBasecode().setCode("C20").setValue("상무").setSeq(1).setEnabled(true);

        DtorBasecode D10 = new DtorBasecode().setCode("D10").setValue("이사").setSeq(1).setEnabled(true);
        DtorBasecode D20 = new DtorBasecode().setCode("D20").setValue("이사대우").setSeq(1).setEnabled(true);

        DtorBasecode E10 = new DtorBasecode().setCode("E10").setValue("부장").setSeq(1).setEnabled(true);
        DtorBasecode E20 = new DtorBasecode().setCode("E20").setValue("차장").setSeq(1).setEnabled(true);
        DtorBasecode E30 = new DtorBasecode().setCode("E30").setValue("과장").setSeq(1).setEnabled(true);
        DtorBasecode E40 = new DtorBasecode().setCode("E40").setValue("대리").setSeq(1).setEnabled(true);
        DtorBasecode E50 = new DtorBasecode().setCode("E50").setValue("사원").setSeq(1).setEnabled(true);

        List<DtorBasecode> dtos = Arrays.asList(A10, A20, B10, B20, C10, C20, D10, D20, E10, E20, E30, E40, E50);

        try {
            repo.saveAll( dtos.stream().map(e -> converter.getBasecode( e.setSeq(dtos.indexOf(e) + 1) ).setType(BasecodeType.JOB_TITLE) ).collect(Collectors.toList()) );
            log.warn("job title basecode created.");

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("job title basecode initialization expcetion.");
        }
    }

    private void setDayoff() {
        if(getCodeStream(BasecodeType.DAYOFF).count() > 0)
            return;
        
        DtorBasecode code100 = new DtorBasecode().setCode("100").setValue("연차").setSeq(1).setEnabled(true);

        DtorBasecode code110 = new DtorBasecode().setCode("110").setValue("반차(오전)").setSeq(1).setEnabled(true);
        DtorBasecode code120 = new DtorBasecode().setCode("120").setValue("반차(오후)").setSeq(1).setEnabled(true);

        DtorBasecode code150 = new DtorBasecode().setCode("150").setValue("경조휴가").setSeq(1).setEnabled(true);
        DtorBasecode code190 = new DtorBasecode().setCode("190").setValue("장기근속휴가").setSeq(1).setEnabled(true);

        DtorBasecode code200 = new DtorBasecode().setCode("200").setValue("대체휴가").setSeq(1).setEnabled(true);
        DtorBasecode code201 = new DtorBasecode().setCode("201").setValue("단축근무일").setSeq(1).setEnabled(true);

        DtorBasecode code300 = new DtorBasecode().setCode("300").setValue("임신 12주 이내(단축근무)").setSeq(1).setEnabled(true);
        DtorBasecode code301 = new DtorBasecode().setCode("301").setValue("임신 36주 이내(단축근무)").setSeq(1).setEnabled(true);
        DtorBasecode code302 = new DtorBasecode().setCode("302").setValue("산전 후 휴가").setSeq(1).setEnabled(true);
        DtorBasecode code303 = new DtorBasecode().setCode("303").setValue("산전 후 휴가(무급)").setSeq(1).setEnabled(true);

        DtorBasecode code304 = new DtorBasecode().setCode("304").setValue("배우자 출산휴가").setSeq(1).setEnabled(true);
        DtorBasecode code305 = new DtorBasecode().setCode("305").setValue("태아 검진 휴가").setSeq(1).setEnabled(true);

        DtorBasecode code400 = new DtorBasecode().setCode("400").setValue("육아휴직").setSeq(1).setEnabled(true);
        DtorBasecode code401 = new DtorBasecode().setCode("401").setValue("육아휴직(무급)").setSeq(1).setEnabled(true);
        DtorBasecode code402 = new DtorBasecode().setCode("402").setValue("돌봄휴가(무급)").setSeq(1).setEnabled(true);

        DtorBasecode code500 = new DtorBasecode().setCode("500").setValue("유급휴가").setSeq(1).setEnabled(true);
        DtorBasecode code501 = new DtorBasecode().setCode("501").setValue("무급휴가").setSeq(1).setEnabled(true);
        DtorBasecode code502 = new DtorBasecode().setCode("502").setValue("병가").setSeq(1).setEnabled(true);
        DtorBasecode code503 = new DtorBasecode().setCode("503").setValue("산재").setSeq(1).setEnabled(true);

        DtorBasecode code600 = new DtorBasecode().setCode("600").setValue("조퇴").setSeq(1).setEnabled(true);
        DtorBasecode code601 = new DtorBasecode().setCode("601").setValue("외출").setSeq(1).setEnabled(true);
        DtorBasecode code602 = new DtorBasecode().setCode("602").setValue("훈련").setSeq(1).setEnabled(true);

        DtorBasecode code900 = new DtorBasecode().setCode("900").setValue("보건휴가").setSeq(1).setEnabled(true);

        List<DtorBasecode> dtos = Arrays.asList(
            code100, code110, code120, code150, code190,
            code200, code201,
            code300, code301, code302, code303, code304, code305,
            code400, code401, code402,
            code500, code501, code502, code503,
            code600, code601, code602,
            code900
        );

        try {
            repo.saveAll( dtos.stream().map(e -> converter.getBasecode( e.setSeq(dtos.indexOf(e) + 1) ).setType(BasecodeType.DAYOFF) ).collect(Collectors.toList()) );
            log.warn("dayoff basecode created.");

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("dayoff basecode initialization expcetion.");
        }
    }

    private void setOvertime() {
        if(getCodeStream(BasecodeType.OVERTIME).count() > 0)
            return;

        DtorBasecode code100 = new DtorBasecode().setCode("100").setValue("조기 출근").setSeq(1).setEnabled(true);
        DtorBasecode code110 = new DtorBasecode().setCode("110").setValue("평일 연장").setSeq(2).setEnabled(true);
        DtorBasecode code120 = new DtorBasecode().setCode("120").setValue("휴일 근무").setSeq(3).setEnabled(true);

        List<DtorBasecode> dtos = Arrays.asList(code100, code110, code120);

        try {
            repo.saveAll( dtos.stream().map(e -> converter.getBasecode(e).setType(BasecodeType.OVERTIME) ).collect(Collectors.toList()) );
            log.warn("overtime basecode created.");

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("overtime basecode initialization expcetion.");
        }
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- initialization ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //




    
    private Stream<Basecode> getCodeStream(BasecodeType type) {
        return repo.findByType(type).stream().sorted(Comparator.comparing(Basecode::getSeq));
    }


    public List<BasecodeType> getAllTypes() {
        return Arrays.stream(BasecodeType.values()).sorted(Comparator.comparing(BasecodeType::getTitle)).collect(Collectors.toList());
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
