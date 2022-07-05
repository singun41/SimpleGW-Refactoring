package com.project.simplegw.document.approval.dtos.receive.docs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.project.simplegw.system.vos.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString(exclude = "content")
public class DtorTempDefaultReport {
    // DtorDocs 객체를 직접 멤버변수로 사용하면 컨트롤러에서 @Validated로 체크가 안 된다.

    @NotBlank(message = "제목을 작성하세요.")
    @Size(max = Constants.COLUMN_LENGTH_DOCU_TITLE, message = "제목을 " + Constants.COLUMN_LENGTH_DOCU_TITLE + " 자 이하로 작성하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    // 임시저장은 결재라인을 저장하지 않는다.
}
