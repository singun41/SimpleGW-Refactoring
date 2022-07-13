package com.project.simplegw.document.approval.entities.details;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.system.vos.Constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)   // entity의 기본 생성자는 반드시 public or protected 이어야 한다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "approval_dayoff", indexes = @Index(columnList = "docs_id"))
public class Dayoff extends DetailsCommon <Dayoff> {
    @Column(name = "seq", nullable = false, updatable = false)
    private int seq;

    @Column(name = "code", nullable = false, updatable = true, length = Constants.COLUMN_LENGTH_BASECODE_CODE)
    private String code;

    @Column(name = "date_start", nullable = false, updatable = false, columnDefinition = Constants.COLUMN_DEFINE_DATE)
    private LocalDate dateStart;

    @Column(name = "date_end", nullable = false, updatable = false, columnDefinition = Constants.COLUMN_DEFINE_DATE)
    private LocalDate dateEnd;

    @Column(name = "duration", nullable = false, updatable = false)
    private int duration;

    // 연차 = 1, 반차 = 0.5 등 소수점 계산을 위해
    // MS-SQL의 float이 java의 double로 매핑되어 double로 처리.
    @Column(name = "count", nullable = false, updatable = false)
    private double count;


    @Override
    public Dayoff bindDocs(Docs docs) {
        this.docs = docs;
        return this;
    }
}
