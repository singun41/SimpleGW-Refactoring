package com.project.simplegw.document.vos;

import com.project.simplegw.system.vos.Menu;

public enum DocsType {
    NOTICE(DocsGroup.BOARD, Menu.NOTICE),
    FREEBOARD(DocsGroup.BOARD, Menu.FREEBOARD),
    ARCHIVE(DocsGroup.BOARD, Menu.ARCHIVE),
    SUGGESTION(DocsGroup.BOARD, Menu.SUGGESTION),

    MEETING(DocsGroup.BOARD, Menu.MEETING_MINUTES),

    ALL(DocsGroup.APPROVAL, Menu.APPROVAL_SEARCH),
    DEFAULT(DocsGroup.APPROVAL, Menu.APPROVAL_DEFAULT),
    COOPERATION(DocsGroup.APPROVAL, Menu.APPROVAL_COOPERATION)
    ;

    // 작업 예정.
    // DAYOFF(DocsGroup.APPROVAL, "휴가 신청서"),
    // OVERTIME(DocsGroup.APPROVAL, "연장 근무 신청서"),
    // PURCHASE(DocsGroup.APPROVAL, "물품 구매 신청서"),
    // NAMECARD(DocsGroup.APPROVAL, "명함 신청서");

    
    private DocsGroup group;
    private Menu menu;
    private String title;

    private DocsType(DocsGroup group, Menu menu) {
        this.group = group;
        this.menu = menu;
        this.title = menu == Menu.APPROVAL_SEARCH ? "문서 전체" : menu.getTitle();
        // 결재문서 검색 메뉴에서 문서 종류에 전체를 표기하기 위해.
    }

    public DocsGroup getGroup() {
        return this.group;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public String getTitle() {
        return this.title;
    }
}
