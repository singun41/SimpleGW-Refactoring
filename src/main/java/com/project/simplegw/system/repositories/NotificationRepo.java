package com.project.simplegw.system.repositories;

import java.util.List;

import com.project.simplegw.system.entities.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    long countByMemberIdAndChecked(Long memberId, boolean checked);
    List<Notification> findByMemberIdOrderByIdDesc(Long memberId);
}
