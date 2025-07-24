package com.example.repository;

import com.example.entity.ChatMessageEntity;
import com.example.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

   /* @Query("SELECT m FROM ChatMessageEntity m WHERE " +
            "(m.sender.id = :senderId AND m.recipient.id = :recipientId) OR " +
            "(m.sender.id = :recipientId AND m.recipient.id = :senderId) " +
            "ORDER BY m.sentTime DESC")
    Page<ChatMessageEntity> findBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
            @Param("senderId") Long senderId,
            @Param("recipientId") Long recipientId,
            @Param("senderId") Long senderId2,
            @Param("recipientId") Long recipientId2,
            Pageable pageable);*/

    @Query("SELECT cm FROM ChatMessageEntity cm " +
            "JOIN FETCH cm.sender s " +
            "JOIN FETCH cm.recipient r " +
            "WHERE (s.id = :user1Id AND r.id = :user2Id) OR (s.id = :user2Id AND r.id = :user1Id) " +
            "ORDER BY cm.sentTime DESC")
    PageImpl<ChatMessageEntity> findChatHistoryBetweenUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id, Pageable pageable);

    @Query("SELECT DISTINCT CASE WHEN m.sender.id = :profileId THEN m.recipient ELSE m.sender END " +
            "FROM ChatMessageEntity m " +
            "WHERE m.sender.id = :profileId OR m.recipient.id = :profileId " +
            "ORDER BY m.sentTime DESC")
    List<ProfileEntity> findRecentChatPartners(@Param("profileId") Long profileId);

    @Query("SELECT cm FROM ChatMessageEntity cm where cm.id=:messageId")
    Optional<ChatMessageEntity> getByMessageId(@Param("messageId")Long messageId);
}
