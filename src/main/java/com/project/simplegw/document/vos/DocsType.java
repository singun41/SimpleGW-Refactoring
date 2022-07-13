package com.project.simplegw.document.vos;

import com.project.simplegw.system.vos.Menu;

public enum DocsType {
    NOTICE(DocsGroup.BOARD, Menu.NOTICE, true),
    FREEBOARD(DocsGroup.BOARD, Menu.FREEBOARD, true),
    ARCHIVE(DocsGroup.BOARD, Menu.ARCHIVE, true),
    SUGGESTION(DocsGroup.BOARD, Menu.SUGGESTION, true),

    MEETING(DocsGroup.BOARD, Menu.MEETING_MINUTES, true),

    ALL(DocsGroup.APPROVAL, Menu.APPROVAL_SEARCH, false),
    DEFAULT(DocsGroup.APPROVAL, Menu.APPROVAL_DEFAULT, true),
    COOPERATION(DocsGroup.APPROVAL, Menu.APPROVAL_COOPERATION, true),
    DAYOFF(DocsGroup.APPROVAL, Menu.APPROVAL_DAYOFF, false)
    ;

    // 작업 예정.
    // DAYOFF(DocsGroup.APPROVAL, "휴가 신청서"),
    // OVERTIME(DocsGroup.APPROVAL, "연장 근무 신청서"),
    // PURCHASE(DocsGroup.APPROVAL, "물품 구매 신청서"),
    // NAMECARD(DocsGroup.APPROVAL, "명함 신청서");

    
    private DocsGroup group;
    private Menu menu;
    private boolean useEditors;
    private String title;

    private DocsType(DocsGroup group, Menu menu, boolean useEditors) {
        this.group = group;
        this.menu = menu;
        this.useEditors = useEditors;
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

    public boolean useEditors() {
        return this.useEditors;
    }
}
