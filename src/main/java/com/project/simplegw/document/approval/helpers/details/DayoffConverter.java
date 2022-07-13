package com.project.simplegw.document.approval.helpers.details;

import org.mapstruct.Mapper;

import com.project.simplegw.document.approval.dtos.receive.details.dayoff.DtorDayoff;
import com.project.simplegw.document.approval.dtos.receive.details.dayoff.DtorDayoffDetails;
import com.project.simplegw.document.approval.entities.details.Dayoff;
import com.project.simplegw.document.dtos.receive.DtorDocs;

@Mapper(componentModel = "spring")
public interface DayoffConverter {
    DtorDocs getDtorDocs(DtorDayoff dto);
    Dayoff getEntity(DtorDayoffDetails dto);
}
