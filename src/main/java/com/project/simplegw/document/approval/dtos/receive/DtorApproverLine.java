package com.project.simplegw.document.approval.dtos.receive;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DtorApproverLine {
    // 결재문서 등록할 때 결재라인을 전달하는 dto
    
    @NotEmpty(message = "결재자를 지정하세요.")
    private Long[] arrApproverId;

    // 참조자는 없는 경우도 허용하므로 검증하지 않음.
    private Long[] arrReferrerId;
}
