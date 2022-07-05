package com.project.simplegw.member.dtos.admin.receive;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.project.simplegw.system.helpers.DateValid;
import com.project.simplegw.system.helpers.Enum;
import com.project.simplegw.system.vos.Constants;
import com.project.simplegw.system.vos.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DtorMemberUpdate {
    // 관리자가 계정 정보 업데이트 시 사용: 패스워드는 분리.

    @NotBlank
    @Enum(enumClass = Role.class, ignoreCase = true, message = "권한을 지정하세요.")
    private String role;

    private boolean enabled;
    
    @NotBlank(message = "부서를 입력하세요.")
    @Size(max = Constants.COLUMN_LENGTH_TEAM, message = "부서명을 " + Constants.COLUMN_LENGTH_TEAM + "자 이하로 작성하세요.")
    private String team;

    @NotBlank(message = "직위를 입력하세요.")
    @Size(max = Constants.COLUMN_LENGTH_JOB_TITLE, message = "직위명을 " + Constants.COLUMN_LENGTH_JOB_TITLE + " 자 이하로 작성하세요.")
    private String jobTitle;

    @NotBlank(message = "이름을 입력하세요.")
    @Size(max = Constants.COLUMN_LENGTH_NAME, message = "이름을 " + Constants.COLUMN_LENGTH_NAME + " 자 이하로 작성하세요.")
    private String name;

    // 아래 필드는 필수가 아닌 옵션, null 허용이 되는 valid로 작성.
    @Size(max = Constants.COLUMN_LENGTH_NAME, message = "영문 이름을 " + Constants.COLUMN_LENGTH_NAME + " 자 이하로 작성하세요.")
    private String nameEng;

    @Pattern(regexp = Constants.REGEXP_MOBILE_NO, message = "핸드폰 번호를 000-0000-0000 형식으로 입력하세요.")
    private String mobile;

    @Pattern(regexp = "\\d{1,4}", message = "내선번호를 1~4자리로 입력하세요.")
    private String tel;

    @Pattern(regexp = Constants.REGEXP_EMAIL, message = "이메일 주소를 정확히 입력하세요.")
    private String email;
    private boolean emailUse;

    // LocalDate로 처리하면 front에서 입력되는 json string value가 날짜 범위를 벗어나는 경우
    // 예를 들면 1월 33일 같은 경우 exception되고 정확한 메시징 처리가 안 되기 때문에
    // 커스텀 valid를 쓰고 String type으로 받는다.
    @DateValid   // null 허용
    private String birthday;

    @DateValid   // null 허용
    private String dateHire;

    @DateValid   // null 허용
    private String dateRetire;
    private boolean retired;
}
