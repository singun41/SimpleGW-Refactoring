package com.project.simplegw.document.vos;

import com.project.simplegw.system.vos.Menu;

public enum DocsType {
    NOTICE(DocsGroup.BOARD, Menu.NOTICE.getTitle()),
    FREEBOARD(DocsGroup.BOARD, Menu.FREEBOARD.getTitle()),
    ARCHIVE(DocsGroup.BOARD, Menu.ARCHIVE.getTitle()),
    SUGGESTION(DocsGroup.BOARD, Menu.SUGGESTION.getTitle()),

    MEETING(DocsGroup.BOARD, Menu.MEETING_MINUTES.getTitle()),

    ALL(DocsGroup.APPROVAL, "전체"),
    DEFAULT(DocsGroup.APPROVAL, Menu.APPROVAL_DEFAULT.getTitle()),
    COOPERATION(DocsGroup.APPROVAL, "업무협조전"),
    DAYOFF(DocsGroup.APPROVAL, "휴가 신청서"),
    OVERTIME(DocsGroup.APPROVAL, "연장 근무 신청서"),
    PURCHASE(DocsGroup.APPROVAL, "물품 구매 신청서"),
    NAMECARD(DocsGroup.APPROVAL, "명함 신청서");

    
    private DocsGroup group;
    private String title;

    private DocsType(DocsGroup group, String title) {
        this.group = group;
        this.title = title;
    }

    public DocsGroup getGroup() {
        return this.group;
    }

    public String getTitle() {
        return this.title;
    }
}
