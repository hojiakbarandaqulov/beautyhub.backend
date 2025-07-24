package com.example.repository;

import com.example.entity.ChatMessageEntity;
import com.example.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    @Query("SELECT m FROM ChatMessageEntity m WHERE " +
            "(m.sender.id = :senderId AND m.recipient.id = :recipientId) OR " +
            "(m.sender.id = :recipientId AND m.recipient.id = :senderId) " +
            "ORDER BY m.sentTime DESC")
    Page<ChatMessageEntity> findBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
            @Param("senderId") Long senderId,
            @Param("recipientId") Long recipientId,
            @Param("senderId") Long senderId2,
            @Param("recipientId") Long recipientId2,
            Pageable pageable);

    @Query("SELECT DISTINCT CASE WHEN m.sender.id = :profileId THEN m.recipient ELSE m.sender END " +
            "FROM ChatMessageEntity m " +
            "WHERE m.sender.id = :profileId OR m.recipient.id = :profileId " +
            "ORDER BY m.sentTime DESC")
    List<ProfileEntity> findRecentChatPartners(@Param("profileId") Long profileId);
}
