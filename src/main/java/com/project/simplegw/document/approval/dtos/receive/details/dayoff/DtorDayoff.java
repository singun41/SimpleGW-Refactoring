package com.project.simplegw.document.approval.dtos.receive.details.dayoff;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.project.simplegw.document.approval.dtos.receive.details.DtorDetailsCommon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class DtorDayoff extends DtorDetailsCommon {
    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @Size(min = 1, message = "휴가 데이터를 추가하세요.")
    @NotNull(message = "휴가 데이터를 추가하세요.")
    private List<DtorDayoffDetails> details;
}
