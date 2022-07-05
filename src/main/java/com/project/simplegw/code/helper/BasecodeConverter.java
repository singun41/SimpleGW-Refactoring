package com.project.simplegw.code.helper;

import com.project.simplegw.code.dtos.receive.DtorBasecode;
import com.project.simplegw.code.dtos.send.DtosBasecode;
import com.project.simplegw.code.entities.Basecode;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BasecodeConverter {
    Basecode getBasecode(DtorBasecode dto);
    DtosBasecode getDtosBasecode(Basecode entity);
}
