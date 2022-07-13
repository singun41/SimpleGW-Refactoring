package com.project.simplegw.document.approval.dtos.send;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class DtosDefaultReport extends DtosApprovalDocsCommon {
    // 디테일이 없는 기본 문서이므로 그대로 전달.
}
