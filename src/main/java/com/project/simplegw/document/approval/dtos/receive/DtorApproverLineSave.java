package com.project.simplegw.document.approval.dtos.receive;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.project.simplegw.system.vos.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DtorApproverLineSave {
    // 결재라인 설정 화면에서 결재라인 저장시 프론트로부터 받는 dto

    @NotBlank(message = "결재라인 타이틀을 작성하세요.")
    @Size(max = Constants.COLUMN_LENGTH_DOCU_TITLE, message = "결재라인 타이틀을 " + Constants.COLUMN_LENGTH_DOCU_TITLE + " 자 이하로 작성하세요.")
    private String title;

    @NotEmpty(message = "결재자를 지정하세요.")
    private Long[] arrApproverId;

    // 참조자는 없는 경우도 허용하므로 검증하지 않음.
    private Long[] arrReferrerId;
}
