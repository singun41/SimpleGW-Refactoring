package com.project.simplegw.document.approval.helpers;

import com.project.simplegw.document.approval.dtos.receive.docs.DtorTempDefaultReport;
import com.project.simplegw.document.approval.dtos.send.DtosApprovalDocs;
import com.project.simplegw.document.approval.dtos.send.DtosApprovalDocsMin;
import com.project.simplegw.document.approval.dtos.send.DtosApprover;
import com.project.simplegw.document.approval.dtos.send.DtosMember;
import com.project.simplegw.document.approval.dtos.send.DtosReferrer;
import com.project.simplegw.document.approval.dtos.send.DtosSavedLine;
import com.project.simplegw.document.approval.entities.ApprovalStatus;
import com.project.simplegw.document.approval.entities.Approver;
import com.project.simplegw.document.approval.entities.ApproverLine;
import com.project.simplegw.document.approval.entities.Referrer;
import com.project.simplegw.document.dtos.receive.DtorDocs;
import com.project.simplegw.document.dtos.send.DtosDocs;
import com.project.simplegw.member.data.MemberData;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApprovalConverter {
    DtosSavedLine getDto(ApproverLine entity);

    DtosMember getDto(MemberData memberData);

    DtorDocs getDtorDocs(DtorTempDefaultReport dto);

    DtosApprovalDocs getDtosApprovalDocs(DtosDocs dto);

    DtosApprover getDtosApprover(Approver entity);
    DtosReferrer getDtosReferrer(Referrer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "team", target = "approverTeam")
    @Mapping(source = "jobTitle", target = "approverJobTitle")
    @Mapping(source = "name", target = "approverName")
    DtosApprovalDocsMin getDtosApprovalDocsMin(ApprovalStatus entity);
}
