package com.project.simplegw.document.approval.entities.details;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.project.simplegw.document.entities.Docs;
import com.project.simplegw.system.vos.Constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString(exclude = {"docs"})   // lazy loading 이기 때문에 제외하지 않으면 no session 에러가 난다.
@NoArgsConstructor(access = AccessLevel.PUBLIC)   // entity의 기본 생성자는 반드시 public or protected 이어야 한다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "approval_dayoff", indexes = @Index(columnList = "docs_id"))
public class Dayoff {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docs_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Docs docs;

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



    public Dayoff updateDocs(Docs docs) {
        this.docs = docs;
        return this;
    }

    public Dayoff updateSeq(int seq) {
        this.seq = seq;
        return this;
    }
}
