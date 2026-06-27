package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {


        @Query(value = "SELECT * FROM messages WHERE (sender_id = ?1 AND receiver_id = ?2) OR (sender_id = ?2 AND receiver_id = ?1) ORDER BY sent_at DESC", nativeQuery = true)
        public List<Message> getMessages(Long user_id1, Long user_id2);

        @Query(value = "SELECT DISTINCT sender_id FROM messages WHERE receiver_id = ?1 AND is_read = false", nativeQuery = true)
        public List<Long> getUnreadChatIds(Long user_id);

        @Query(value = "SELECT sender_id FROM messages WHERE receiver_id = ?1 UNION SELECT receiver_id FROM messages WHERE sender_id = ?1", nativeQuery = true)
        public List<Long> getAllChats(Long user_id);

        @Modifying
        @Query(value = "UPDATE messages SET is_read = true WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
        public void markMessageAsRead(Long sender_id, Long receiver_id);

        @Query(value = "SELECT * FROM messages m " +
                "INNER JOIN (SELECT " +
                "MAX(sent_at) AS latest_time, " +
                "LEAST(sender_id, receiver_id) AS user1, " +
                "GREATEST(sender_id, receiver_id) AS user2 " +
                "FROM messages WHERE sender_id = ?1 OR receiver_id = ?1 " +
                "GROUP BY user1, user2) " +
                "AS temp_table " +
                "ON LEAST(m.sender_id, m.receiver_id) = temp_table.user1 " +
                "AND GREATEST(m.sender_id, m.receiver_id) = temp_table.user2 " +
                "AND m.sent_at = temp_table.latest_time " +
                "WHERE m.sender_id = ?1 OR m.receiver_id = ?1 " +
                "ORDER BY m.sent_at DESC", nativeQuery = true)
        public List<Message> getInboxPreviewTexts(Long user_id);

}
