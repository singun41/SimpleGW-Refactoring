package com.project.simplegw.document.approval.helpers;

import java.math.BigInteger;

import com.project.simplegw.document.approval.dtos.send.DtosApprovalDocsMin;
import com.project.simplegw.document.approval.vos.Sign;
import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.member.data.MemberData;

public class DtosApprovalDocsMinConverter {
    // 받은 결재문서의 기간 검색 후 바인딩(ApproverService, ReferrerService에서 호출)
    public static DtosApprovalDocsMin fromObjs(Object[] objs, MemberData approverInfo) {
        BigInteger bigId = (BigInteger) objs[0];

        return new DtosApprovalDocsMin()
            .setId( bigId.longValue() )
            
            .updateDocsType(DocsType.valueOf((String) objs[1]))
            .setTitle((String) objs[2])
            
            .setWriterTeam((String) objs[3])
            .setWriterJobTitle((String) objs[4])
            .setWriterName((String) objs[5])
            
            .setApproverTeam(approverInfo.getTeam())
            .setApproverJobTitle(approverInfo.getJobTitle())
            .setApproverName(approverInfo.getName())
            
            .setSign(Sign.valueOf((String) objs[7]))

            .setCreatedDate( ((java.sql.Date) objs[8]).toLocalDate() );
    }
}
