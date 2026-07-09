-- =========================================================================
-- 1. CORE IDENTITY & SOCIAL GRAPH
-- =========================================================================

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       is_admin BOOLEAN DEFAULT FALSE,
                       token_version INT NOT NULL DEFAULT 0,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friendships (
                             user_id INT NOT NULL,
                             friend_id INT NOT NULL,
                             status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
                             established_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (user_id, friend_id),
                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                             FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Base Table for the internal mail system (3NF Decomposition)
CREATE TABLE messages (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          sender_id INT NOT NULL,
                          receiver_id INT NOT NULL,
                          msg_type ENUM('friend_request', 'challenge', 'note') NOT NULL,
                          content TEXT DEFAULT NULL, -- Primarily used for 'note' type
                          is_read BOOLEAN DEFAULT FALSE,
                          sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
                          FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================================================
-- 2. ADMINISTRATION
-- =========================================================================

CREATE TABLE announcements (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               creator_id INT NOT NULL,
                               title VARCHAR(255) NOT NULL,
                               content TEXT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================================================
-- 3. QUIZ ENGINE
-- =========================================================================

CREATE TABLE quizzes (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         creator_id INT,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         is_randomized BOOLEAN DEFAULT FALSE,
                         is_single_page BOOLEAN DEFAULT TRUE,
                         immediate_feedback BOOLEAN DEFAULT FALSE,
                         allow_practice BOOLEAN DEFAULT TRUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE challenges (
                            message_id INT PRIMARY KEY,
                            quiz_id INT NOT NULL,
                            challenge_score INT NOT NULL,
                            FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
                            FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE questions (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           quiz_id INT NOT NULL,
                           q_type ENUM('STANDARD', 'FILL_IN_THE_BLANK', 'MULTIPLE_CHOICE', 'PICTURE_RESPONSE') NOT NULL,
                           prompt TEXT NOT NULL,
                           image_url VARCHAR(255) DEFAULT NULL,
                           sequence_num INT DEFAULT 0,
                           FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
                           INDEX idx_quiz_questions (quiz_id)
);

CREATE TABLE answers (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         question_id INT NOT NULL,
                         answer_text VARCHAR(255) NOT NULL,
                         is_correct BOOLEAN DEFAULT FALSE,
                         slot_num INT DEFAULT 0,
                         FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- =========================================================================
-- 4. GAMIFICATION & PERFORMANCE TRACKING
-- =========================================================================

CREATE TABLE quiz_attempts (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT,
                               quiz_id INT NOT NULL,
                               score INT NOT NULL,
                               time_taken_sec INT NOT NULL,
                               is_practice BOOLEAN DEFAULT FALSE,
                               taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
                               FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
                               INDEX idx_leaderboard (quiz_id, is_practice, score DESC, time_taken_sec ASC),
                               INDEX idx_user_history (user_id, taken_at DESC)
);

CREATE TABLE user_achievements (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   user_id INT NOT NULL,
                                   achievement_type ENUM('amateur_author', 'prolific_author', 'prodigious_author', 'quiz_machine', 'greatest', 'practice_perfect') NOT NULL,
                                   earned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                   UNIQUE KEY uk_user_achievement (user_id, achievement_type)
);