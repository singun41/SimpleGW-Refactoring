package com.project.simplegw.member.helpers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.project.simplegw.member.dtos.send.DtosDayoffCnt;
import com.project.simplegw.member.entities.MemberAddOn;

@Mapper(componentModel = "spring")
public interface MemberAddOnConverter {
    @Mapping(target = "usable", source = "dayoffUsableCnt")
    @Mapping(target = "use", source = "dayoffUseCnt")
    DtosDayoffCnt getDayoffCnt(MemberAddOn entity);
}
