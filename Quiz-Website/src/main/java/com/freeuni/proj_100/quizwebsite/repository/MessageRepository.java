package com.freeuni.proj_100.quizwebsite.repository;

import com.freeuni.proj_100.quizwebsite.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for the Message entity, providing database access for the 'messages' table.
 * Extends JpaRepository for standard CRUD operations, and defines five custom native queries:
 *      - get all messages between 'user_id1' and 'user_id2'
 *      - get users that 'user_id' hasn't read the last messages yet
 *      - get users that 'user_id' had chat with (Any direction)
 *      - flip isRead value in database to true
 *      - get last messages sent or received in each chats to display in inbox
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

        /// get all messages between 'user_id1' and 'user_id2'
        @Query(value = "SELECT * FROM messages WHERE (sender_id = ?1 AND receiver_id = ?2) OR (sender_id = ?2 AND receiver_id = ?1) ORDER BY sent_at DESC", nativeQuery = true)
        public List<Message> getMessages(int user_id1, int user_id2);

        /// get IDs of users who have sent unread messages to 'user_id'
        @Query(value = "SELECT DISTINCT sender_id FROM messages WHERE receiver_id = ?1 AND is_read = false", nativeQuery = true)
        public List<Integer> getUnreadChatIds(int user_id);

        /// get all user IDs that 'user_id' had chat with (Any direction)
        @Query(value = "SELECT sender_id FROM messages WHERE receiver_id = ?1 UNION SELECT receiver_id FROM messages WHERE sender_id = ?1", nativeQuery = true)
        public List<Integer> getAllChats(int user_id);

        /// Mark all unread messages from 'sender_id' to 'receiver_id' as read
        @Modifying(clearAutomatically = true)
        @Query(value = "UPDATE messages SET is_read = true WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
        public void markMessageAsRead(int sender_id, int receiver_id);

        /// Get last messages from every user that 'user_id' had a chat with to display in inbox section
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
        public List<Message> getInboxPreviewTexts(int user_id);

}
