package com.project.simplegw.document.dtos.admin.receive;

import com.project.simplegw.document.vos.EditorDocs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DtorEditorForm {
    private EditorDocs docs;
    private String content;
}
