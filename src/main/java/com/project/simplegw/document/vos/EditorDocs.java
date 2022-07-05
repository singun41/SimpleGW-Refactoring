package com.project.simplegw.document.vos;

import com.project.simplegw.system.vos.Menu;

public enum EditorDocs {
    // WYSWYG 에디터를 사용하는 문서 리스트를 작성한다.

    NOTICE(Menu.NOTICE.getTitle()), FREEBOARD(Menu.FREEBOARD.getTitle()), SUGGESTION(Menu.SUGGESTION.getTitle()), ARCHIVE(Menu.ARCHIVE.getTitle()),

    MEETING_MINUTES(Menu.MEETING_MINUTES.getTitle()),
    
    APPROVAL_DEFAULT(Menu.APPROVAL_DEFAULT.getTitle())
    ;


    private String title;
    private EditorDocs(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
