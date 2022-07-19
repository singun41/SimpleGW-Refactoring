package com.project.simplegw.document.approval.entities.details.dayoff;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.project.simplegw.document.approval.entities.details.TempDetailsCommon;
import com.project.simplegw.document.entities.TempDocs;
import com.project.simplegw.system.vos.Constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)   // entity의 기본 생성자는 반드시 public or protected 이어야 한다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "temp_approval_dayoff", indexes = @Index(columnList = "docs_id"))
public class TempDayoff extends TempDetailsCommon <TempDayoff> {
    // update는 삭제 후 insert로 처리하므로 updatable을 false로 설정.

    @Column(name = "seq", nullable = false, updatable = false)
    private int seq;

    @Column(name = "code", nullable = false, updatable = false, length = Constants.COLUMN_LENGTH_BASECODE_CODE)
    private String code;

    @Column(name = "date_from", nullable = false, updatable = false, columnDefinition = Constants.COLUMN_DEFINE_DATE)
    private LocalDate dateFrom;

    @Column(name = "date_to", nullable = false, updatable = false, columnDefinition = Constants.COLUMN_DEFINE_DATE)
    private LocalDate dateTo;
    

    @Override
    public TempDayoff bindDocs(TempDocs docs) {
        this.docs = docs;
        return this;
    }

    public TempDayoff updateSeq(int seq) {
        this.seq = seq;
        return this;
    }
}
