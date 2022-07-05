package com.project.simplegw.member.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.project.simplegw.system.vos.Constants;
import com.project.simplegw.system.vos.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString(exclude = "password")
@NoArgsConstructor(access = AccessLevel.PUBLIC)   // entity의 기본 생성자는 반드시 public or protected 이어야 한다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "member", indexes = @Index(columnList = "user_id"))
public class Member {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false, length = Constants.COLUMN_LENGTH_USER_ID, unique = true)
    private String userId;   // Repository에서 findById 메서드를 사용할 때 헷갈릴 수 있어 userId로 작성.

    @Column(name = "password", nullable = false, updatable = true, length = Constants.COLUMN_LENGTH_PW)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = true, length = Constants.COLUMN_LENGTH_ROLE)
    // @ColumnDefault(value = "'USER'")   // DB의 계정명 simplegw가 입력된다. MemberService에서 핸들링하기 위해 주석.
    private Role role;

    @Column(name = "enabled", nullable = false, updatable = true)
    // @ColumnDefault(value = "1")   // boolean 기본값 false가 입력된다. MemberService에서 핸들링하기 위해 주석.
    private boolean enabled;


    public Member updatePw(String pw) {
        if(pw == null || pw.isBlank())
            return this;
        this.password = pw;
        return this;
    }

    public Member updateRole(Role role) {
        if(role != null)
            this.role = role;
        return this;
    }
    
    public Member updateEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
