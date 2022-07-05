package com.project.simplegw.member.dtos.receive;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
public class DtorMyDetails {
    // 유저가 개인정보 수정시 사용.

    // null 허용
    @Size(max = Constants.COLUMN_LENGTH_NAME, message = "영문 이름을 " + Constants.COLUMN_LENGTH_NAME + " 자 이하로 작성하세요.")
    private String nameEng;

    @NotBlank(message = "핸드폰 번호를 입력하세요.")
    @Pattern(regexp = Constants.REGEXP_MOBILE_NO, message = "핸드폰 번호를 000-0000-0000 형식으로 입력하세요.")
    private String mobile;

    
    // LocalDate로 처리하면 front에서 입력되는 json string value가 날짜 범위를 벗어나는 경우
    // 예를 들면 1월 33일 같은 경우 exception되고 정확한 메시징 처리가 안 되기 때문에
    // 커스텀 valid를 쓰고 String type으로 받는다.
    @DateValid(message = "생일을 정확히 입력하세요.")   // null 허용
    // @Past(message = "생일을 정확히 입력하세요.")   문자열을 입력받지 못하므로 주석.
    private String birthday;
}
