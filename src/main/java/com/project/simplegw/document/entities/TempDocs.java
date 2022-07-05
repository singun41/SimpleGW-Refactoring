package com.project.simplegw.document.entities;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.member.entities.Member;
import com.project.simplegw.system.vos.Constants;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)   // entity의 기본 생성자는 반드시 public or protected 이어야 한다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "temp_docs", indexes = @Index(columnList = "writer_id"))
public class TempDocs {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false, length = Constants.COLUMN_LENGTH_DOCU_TYPE)
    private DocsType type;

    @Column(name = "title", nullable = false, updatable = true, columnDefinition = Constants.COLUMN_DEFINE_TITLE)
    private String title;

    @Column(name = "writer_id", nullable = false, updatable = false)
    private Long writerId;   // 작성자를 찾기 위해서 Member 클래스를 바인딩해두면 소유자 검증할 때마다 Member를 찾는 쿼리가 발생함. id값으로만 해두면 equals 한 번에 처리가 가능함. 성능이점이 있음.

    // 작성 날짜로 검색을 용이하게 하기 위해서 생성 날짜 및 시간은 컬럼을 분리한다.
    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = Constants.COLUMN_DEFINE_DATE)
    @CreationTimestamp
    private LocalDate createdDate;

    @Column(name = "created_time", nullable = false, updatable = false, columnDefinition = Constants.COLUMN_DEFINE_TIME)
    @CreationTimestamp // 저장시 자동으로 현재 시간을 등록해줌. updatable = false 이므로 document entity가 수정될 때에는 update 되지 않음.
    private LocalTime createdTime;





    public TempDocs setWriterId(Member member) {
        this.writerId = member.getId();
        return this;
    }

    public TempDocs updateTitle(String title) {
        if(title != null && !title.isBlank()) {
            this.title = title;
        } else {
            this.title = "empty title";
        }
        return this;
    }
}