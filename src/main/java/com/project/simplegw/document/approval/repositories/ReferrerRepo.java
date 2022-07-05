package com.project.simplegw.document.approval.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.simplegw.document.approval.entities.Referrer;
import com.project.simplegw.document.vos.DocsType;

public interface ReferrerRepo extends JpaRepository<Referrer, Long> {
    List<Referrer> findByDocsIdOrderById(Long docsId);
    List<Referrer> findByMemberIdOrderById(Long memberId);

    long countByMemberIdAndCheckedDatetimeIsNull(Long memberId);   // 참조요청 받은 확인하지 않은 문서 카운트


    // 결재참조로 받은 문서 기간 검색, 결과를 DtosApprovalDocsMin 클래스로 처리하기 위해서 List<Object[]>로 반환
    @Query(
        value = "select b.id, b.type, b.title, b.writer_team, b.writer_job_title, b.writer_name, approver_id = c.member_id, c.sign, b.created_date " +
                "from referrer a " +
                    "join docs b on a.docs_id = b.id " +
                    "join approval_status c on b.id = c.docs_id " +
                "where 1=1 " +
                    " and a.member_id = :#{#referrer_id} " +
                    " and b.[type] = case when :#{#type.name()} = 'ALL' then b.[type] else :#{#type.name()} end " +
                    " and b.created_date between :#{#date_start} and :#{#date_end}",
        nativeQuery = true
    )
    List<Object[]> findForReferrer(@Param("referrer_id") Long referrerId, @Param("type") DocsType type, @Param("date_start") LocalDate dateStart, @Param("date_end") LocalDate dateEnd);
}
