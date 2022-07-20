package com.project.simplegw.member.entities;

import java.util.List;

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

import com.project.simplegw.document.approval.entities.details.dayoff.Dayoff;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString(exclude = "member")   // lazy loading 일 때 제외하지 않으면 no session 에러가 난다.
@NoArgsConstructor(access = AccessLevel.PUBLIC)   // entity의 기본 생성자는 반드시 public or protected 이어야 한다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "member_add_on", indexes = @Index(columnList = "member_id"))
public class MemberAddOn {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One to one 설정해서 memberDetails를 저장할 때 member 엔티티도 자동으로 저장하기 위해 cascade를 설정.... 했으나
    // 비즈니스 로직에서 필요할 때마다 이 엔티티를 호출하는데 거기서 계속 발생할 N+1 문제때문에 Many To One으로 변경한다.
    // 멤버를 신규 등록할 때 반드시 Member 엔티티를 먼저 저장한 뒤 바인딩 시키도록 로직을 구현한다면 문제는 없다.
    // @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false, updatable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "dayoff_usable_cnt", nullable = false, updatable = true)
    private double dayoffUsableCnt;

    @Column(name = "dayoff_use_cnt", nullable = false, updatable = true)
    private double dayoffUseCnt;



    public MemberAddOn resetDayoffUsableCnt(double num) {
        this.dayoffUsableCnt = num;
        return this;
    }

    public MemberAddOn updateDayoffUseCnt(List<Dayoff> dtos) {
        dtos.forEach( e -> this.dayoffUseCnt += e.getCount() );
        return this;
    }
}
