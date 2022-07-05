package com.project.simplegw.member.dtos.send;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DtosMyDetails {   // 유저가 본인 정보를 요청할 때 프론트로 데이터를 보내기 위한 dto
    private String team;
    private String jobTitle;
    private String name;
    private String nameEng;
    private String mobile;
    private String email;
    private String tel;
    private LocalDate birthday;
    private LocalDate dateHire;
}
