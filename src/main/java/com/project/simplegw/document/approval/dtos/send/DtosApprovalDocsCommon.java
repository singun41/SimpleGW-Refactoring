package com.project.simplegw.document.approval.dtos.send;

import java.time.LocalDate;
import java.time.LocalTime;

import com.project.simplegw.document.vos.DocsType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DtosApprovalDocsCommon {
    // 모든 결재문서 데이터를 프론트로 보낼 때 공통으로 사용하는 클래스
    // 각 디테일이 다른 결재문서들은 이 dto를 상속한 뒤 디테일 dto를 리스트로 붙여서 사용.

    private Long id;

    private DocsType type;

    private String title;
    private String content;
    
    private Long writerId;
    private String writerTeam;
    private String writerJobTitle;
    private String writerName;

    private LocalDate createdDate;
    private LocalTime createdTime;

    private DtosApprovalLinePack line;
}
