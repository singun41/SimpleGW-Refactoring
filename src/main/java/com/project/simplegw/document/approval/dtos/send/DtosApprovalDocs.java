package com.project.simplegw.document.approval.dtos.send;

import com.project.simplegw.document.dtos.send.DtosDocs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class DtosApprovalDocs extends DtosDocs {
    private DtosApprovalLinePack line;
}
