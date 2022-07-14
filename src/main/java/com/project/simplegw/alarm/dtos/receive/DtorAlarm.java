package com.project.simplegw.alarm.dtos.receive;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.project.simplegw.system.helpers.DateValid;
import com.project.simplegw.system.vos.Constants;

import lombok.Getter;

@Getter
public class DtorAlarm {
    @NotNull(message = "날짜를 입력하세요.")
    @DateValid   // null 허용되므로 위에서 null 체크
    private String alarmDate;

    @Pattern(regexp = Constants.REGEXP_TIME, message = "시간을 양식(00:00 ~ 23:59)에 맞게 입력하세요.")   // null 허용 되므로 아래에 NotNull 추가
    @NotNull(message = "시간을 입력하세요.")
    private String alarmTime;

    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = Constants.COLUMN_LENGTH_DOCU_TITLE, message = "제목을 " + Constants.COLUMN_LENGTH_DOCU_TITLE + " 자 이하로 작성하세요.")
    private String title;

    @Size(max = Constants.COLUMN_LENGTH_REMARKS, message = "내용은 " + Constants.COLUMN_LENGTH_REMARKS + " 자 이하로 작성하세요.")
    private String remarks;
}
