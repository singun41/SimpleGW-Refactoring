package com.project.simplegw.document.approval.dtos.receive.details.dayoff;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.project.simplegw.system.helpers.DateValid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DtorDayoffDetails {
    @NotNull(message = "휴가 코드를 선택하세요.")
    private String code;

    // LocalDate로 처리하면 front에서 입력되는 json string value가 날짜 범위를 벗어나는 경우
    // 예를 들면 1월 33일 같은 경우 exception되고 정확한 메시징 처리가 안 되기 때문에
    // 커스텀 valid를 쓰고 String type으로 받는다.
    @DateValid(message = "시작 날짜를 선택하세요.")   // null 허용되므로 아래에 not null 추가.
    @NotNull(message = "시작 날짜를 선택하세요.")
    private LocalDate dateStart;

    @DateValid(message = "종료 날짜를 선택하세요.")   // null 허용되므로 아래에 not null 추가.
    @NotNull(message = "종료 날짜를 선택하세요.")
    private LocalDate dateEnd;
}
