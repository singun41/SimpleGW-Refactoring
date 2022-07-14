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

    @NotNull(message = "시작 날짜를 선택하세요.")
    @DateValid   // null 허용되므로 위에서 null 체크
    private LocalDate dateStart;

    @NotNull(message = "종료 날짜를 선택하세요.")
    @DateValid   // null 허용되므로 위에서 null 체크
    private LocalDate dateEnd;
}
