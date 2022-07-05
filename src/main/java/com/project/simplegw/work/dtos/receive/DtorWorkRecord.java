package com.project.simplegw.work.dtos.receive;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.project.simplegw.system.helpers.DateValid;
import com.project.simplegw.system.vos.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DtorWorkRecord {
    // LocalDate로 처리하면 front에서 입력되는 json string value가 날짜 범위를 벗어나는 경우
    // 예를 들면 1월 33일 같은 경우 exception되고 정확한 메시징 처리가 안 되기 때문에
    // 커스텀 valid를 쓰고 String type으로 받는다.
    @DateValid(message = "작성일자를 확인하세요.")
    @NotNull(message = "작성일자를 확인하세요.")
    private String workDate;

    @Size(max = Constants.COLUMN_LENGTH_COMMENT, message = "업무 처리 내용을 " + Constants.COLUMN_LENGTH_COMMENT + " 자 이하로 작성하세요.")
    @NotBlank(message = "업무 처리 내용을 작성하세요.")
    @NotNull(message = "업무 처리 내용을 작성하세요.")
    private String todayWork;

    // null or 공백 허용
    @Size(max = Constants.COLUMN_LENGTH_COMMENT, message = "다음 업무 계획을 " + Constants.COLUMN_LENGTH_COMMENT + " 자 이하로 작성하세요.")
    private String nextPlan;
}
