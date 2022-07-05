package com.project.simplegw.document.dtos.receive;

// import javax.validation.constraints.FutureOrPresent;

import com.project.simplegw.system.helpers.DateValid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DtorDocsOptions {   // 문서 옵션 설정시 프론트로부터 받는 데이터 dto, 업데이트시 docsId는 url로 받아야한다. (RestAPI 표준 설계를 따름.)
    
    private boolean use;

    // LocalDate로 처리하면 front에서 입력되는 json string value가 날짜 범위를 벗어나는 경우
    // 예를 들면 1월 33일 같은 경우 exception되고 정확한 메시징 처리가 안 되기 때문에
    // 커스텀 valid를 쓰고 String type으로 받는다.
    @DateValid   // null 허용
    // @FutureOrPresent(message = "게시 종료일은 오늘 또는 이후 날짜로 설정하세요.")
    private String dueDate;
}
