-- =====================================================
-- DevTrack Database Schema
-- =====================================================

CREATE DATABASE IF NOT EXISTS devtrack;
USE devtrack;

-- =====================================================
-- USERS TABLE
-- =====================================================
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    profile_pic VARCHAR(255) DEFAULT 'default.png',
    streak_count INT DEFAULT 0,
    longest_streak INT DEFAULT 0,
    total_hours_coded DECIMAL(10,2) DEFAULT 0,
    total_problems_solved INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_username (username),
    INDEX idx_email (email)
);

-- =====================================================
-- DAILY_LOGS TABLE
-- =====================================================
CREATE TABLE daily_logs (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    log_date DATE NOT NULL,
    hours_coded DECIMAL(4,2) NOT NULL,
    description TEXT,
    technologies_used VARCHAR(255),
    mood VARCHAR(20) DEFAULT 'OKAY',
    productivity_score INT CHECK (productivity_score BETWEEN 1 AND 10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_date (user_id, log_date),
    INDEX idx_log_date (log_date),
    INDEX idx_user_date (user_id, log_date)
);

-- =====================================================
-- DSA_PROBLEMS TABLE
-- =====================================================
CREATE TABLE dsa_problems (
    problem_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    platform VARCHAR(50) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    topic VARCHAR(100),
    problem_link VARCHAR(500),
    status VARCHAR(20) DEFAULT 'SOLVED',
    time_taken INT,
    approach TEXT,
    notes TEXT,
    solved_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_solved_date (solved_date),
    INDEX idx_difficulty (difficulty),
    INDEX idx_platform (platform)
);

-- =====================================================
-- GOALS TABLE
-- =====================================================
CREATE TABLE goals (
    goal_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    goal_type VARCHAR(20) NOT NULL,
    category VARCHAR(30) NOT NULL,
    target_value INT NOT NULL,
    current_value INT DEFAULT 0,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    completed_date DATE,
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_dates (start_date, end_date),
    INDEX idx_completed (is_completed)
);

-- =====================================================
-- SKILLS TABLE
-- =====================================================
CREATE TABLE skills (
    skill_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    category VARCHAR(30) NOT NULL,
    proficiency VARCHAR(20) DEFAULT 'BEGINNER',
    hours_practiced DECIMAL(10,2) DEFAULT 0,
    projects_count INT DEFAULT 0,
    started_date DATE,
    last_used DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_skill (user_id, skill_name)
);

-- =====================================================
-- NOTES TABLE
-- =====================================================
CREATE TABLE notes (
    note_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    tags VARCHAR(255),
    category VARCHAR(100),
    is_pinned BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_tags (tags)
);

-- =====================================================
-- SAMPLE DATA
-- =====================================================
INSERT INTO users (username, email, password, full_name, streak_count, total_hours_coded, total_problems_solved) 
VALUES ('demo_user', 'demo@devtrack.com', 'demo123', 'Demo Developer', 7, 45.5, 23);

INSERT INTO daily_logs (user_id, log_date, hours_coded, description, technologies_used, mood, productivity_score) VALUES
(1, CURDATE() - INTERVAL 6 DAY, 4.0, 'Worked on Spring Boot APIs', 'Java, Spring Boot', 'GOOD', 7),
(1, CURDATE() - INTERVAL 5 DAY, 5.5, 'Built frontend dashboard', 'HTML, CSS, JavaScript', 'GREAT', 9),
(1, CURDATE() - INTERVAL 4 DAY, 3.0, 'DSA practice session', 'Java, LeetCode', 'OKAY', 6),
(1, CURDATE() - INTERVAL 3 DAY, 6.0, 'Full stack development', 'Java, MySQL, JS', 'GREAT', 8),
(1, CURDATE() - INTERVAL 2 DAY, 4.5, 'Bug fixes and testing', 'Java, JUnit', 'GOOD', 7),
(1, CURDATE() - INTERVAL 1 DAY, 5.0, 'New feature implementation', 'Spring Boot, React', 'GREAT', 8),
(1, CURDATE(), 3.5, 'Code review and documentation', 'Markdown, Git', 'GOOD', 7);

INSERT INTO dsa_problems (user_id, title, platform, difficulty, topic, status, solved_date) VALUES
(1, 'Two Sum', 'LEETCODE', 'EASY', 'Arrays', 'SOLVED', CURDATE() - INTERVAL 5 DAY),
(1, 'Valid Parentheses', 'LEETCODE', 'EASY', 'Stack', 'SOLVED', CURDATE() - INTERVAL 5 DAY),
(1, 'Merge Two Sorted Lists', 'LEETCODE', 'EASY', 'Linked List', 'SOLVED', CURDATE() - INTERVAL 4 DAY),
(1, 'Binary Tree Inorder', 'LEETCODE', 'MEDIUM', 'Trees', 'SOLVED', CURDATE() - INTERVAL 3 DAY),
(1, 'LRU Cache', 'LEETCODE', 'MEDIUM', 'Design', 'SOLVED', CURDATE() - INTERVAL 2 DAY),
(1, 'Word Search', 'LEETCODE', 'MEDIUM', 'Backtracking', 'SOLVED', CURDATE() - INTERVAL 1 DAY),
(1, 'Median of Two Sorted Arrays', 'LEETCODE', 'HARD', 'Binary Search', 'ATTEMPTED', CURDATE());

INSERT INTO goals (user_id, title, goal_type, category, target_value, current_value, start_date, end_date, priority) VALUES
(1, 'Solve 50 LeetCode Problems', 'MONTHLY', 'DSA_PROBLEMS', 50, 23, DATE_FORMAT(CURDATE(), '%Y-%m-01'), LAST_DAY(CURDATE()), 'HIGH'),
(1, 'Code 30 Hours This Week', 'WEEKLY', 'CODING_HOURS', 30, 22, CURDATE() - INTERVAL 6 DAY, CURDATE() + INTERVAL 1 DAY, 'MEDIUM'),
(1, 'Complete DevTrack Project', 'CUSTOM', 'PROJECTS', 100, 75, CURDATE() - INTERVAL 10 DAY, CURDATE() + INTERVAL 5 DAY, 'HIGH');

INSERT INTO skills (user_id, skill_name, category, proficiency, hours_practiced, projects_count, started_date, last_used) VALUES
(1, 'Java', 'LANGUAGE', 'INTERMEDIATE', 150.0, 5, CURDATE() - INTERVAL 180 DAY, CURDATE()),
(1, 'Spring Boot', 'FRAMEWORK', 'INTERMEDIATE', 80.0, 3, CURDATE() - INTERVAL 90 DAY, CURDATE()),
(1, 'JavaScript', 'LANGUAGE', 'INTERMEDIATE', 100.0, 4, CURDATE() - INTERVAL 150 DAY, CURDATE()),
(1, 'MySQL', 'DATABASE', 'INTERMEDIATE', 60.0, 4, CURDATE() - INTERVAL 120 DAY, CURDATE()),
(1, 'React', 'FRAMEWORK', 'BEGINNER', 30.0, 1, CURDATE() - INTERVAL 30 DAY, CURDATE() - INTERVAL 2 DAY);
