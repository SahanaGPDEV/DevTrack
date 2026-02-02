-- Sample data for DevTrack (H2 Database)
-- This runs after Hibernate creates the schema

INSERT INTO users (user_id, username, email, password, full_name, streak_count, longest_streak, total_hours_coded, total_problems_solved, created_at, is_active) 
VALUES (1, 'demo_user', 'demo@devtrack.com', 'demo123', 'Demo Developer', 7, 12, 45.5, 23, CURRENT_TIMESTAMP, true);

INSERT INTO daily_logs (log_id, user_id, log_date, hours_coded, description, technologies_used, mood, productivity_score, created_at) VALUES
(1, 1, DATEADD('DAY', -6, CURRENT_DATE), 4.0, 'Worked on Spring Boot APIs', 'Java, Spring Boot', 'GOOD', 7, CURRENT_TIMESTAMP),
(2, 1, DATEADD('DAY', -5, CURRENT_DATE), 5.5, 'Built frontend dashboard', 'HTML, CSS, JavaScript', 'GREAT', 9, CURRENT_TIMESTAMP),
(3, 1, DATEADD('DAY', -4, CURRENT_DATE), 3.0, 'DSA practice session', 'Java, LeetCode', 'OKAY', 6, CURRENT_TIMESTAMP),
(4, 1, DATEADD('DAY', -3, CURRENT_DATE), 6.0, 'Full stack development', 'Java, MySQL, JS', 'GREAT', 8, CURRENT_TIMESTAMP),
(5, 1, DATEADD('DAY', -2, CURRENT_DATE), 4.5, 'Bug fixes and testing', 'Java, JUnit', 'GOOD', 7, CURRENT_TIMESTAMP),
(6, 1, DATEADD('DAY', -1, CURRENT_DATE), 5.0, 'New feature implementation', 'Spring Boot, React', 'GREAT', 8, CURRENT_TIMESTAMP),
(7, 1, CURRENT_DATE, 3.5, 'Code review and documentation', 'Markdown, Git', 'GOOD', 7, CURRENT_TIMESTAMP);

INSERT INTO dsa_problems (problem_id, user_id, title, platform, difficulty, topic, status, solved_date, created_at) VALUES
(1, 1, 'Two Sum', 'LEETCODE', 'EASY', 'Arrays', 'SOLVED', DATEADD('DAY', -5, CURRENT_DATE), CURRENT_TIMESTAMP),
(2, 1, 'Valid Parentheses', 'LEETCODE', 'EASY', 'Stack', 'SOLVED', DATEADD('DAY', -5, CURRENT_DATE), CURRENT_TIMESTAMP),
(3, 1, 'Merge Two Sorted Lists', 'LEETCODE', 'EASY', 'Linked List', 'SOLVED', DATEADD('DAY', -4, CURRENT_DATE), CURRENT_TIMESTAMP),
(4, 1, 'Binary Tree Inorder', 'LEETCODE', 'MEDIUM', 'Trees', 'SOLVED', DATEADD('DAY', -3, CURRENT_DATE), CURRENT_TIMESTAMP),
(5, 1, 'LRU Cache', 'LEETCODE', 'MEDIUM', 'Design', 'SOLVED', DATEADD('DAY', -2, CURRENT_DATE), CURRENT_TIMESTAMP),
(6, 1, 'Word Search', 'LEETCODE', 'MEDIUM', 'Backtracking', 'SOLVED', DATEADD('DAY', -1, CURRENT_DATE), CURRENT_TIMESTAMP),
(7, 1, 'Median of Two Sorted Arrays', 'LEETCODE', 'HARD', 'Binary Search', 'ATTEMPTED', CURRENT_DATE, CURRENT_TIMESTAMP);

INSERT INTO goals (goal_id, user_id, title, goal_type, category, target_value, current_value, start_date, end_date, priority, is_completed, created_at) VALUES
(1, 1, 'Solve 50 LeetCode Problems', 'MONTHLY', 'DSA_PROBLEMS', 50, 23, DATEADD('DAY', -15, CURRENT_DATE), DATEADD('DAY', 15, CURRENT_DATE), 'HIGH', false, CURRENT_TIMESTAMP),
(2, 1, 'Code 30 Hours This Week', 'WEEKLY', 'CODING_HOURS', 30, 22, DATEADD('DAY', -6, CURRENT_DATE), DATEADD('DAY', 1, CURRENT_DATE), 'MEDIUM', false, CURRENT_TIMESTAMP),
(3, 1, 'Complete DevTrack Project', 'CUSTOM', 'PROJECTS', 100, 75, DATEADD('DAY', -10, CURRENT_DATE), DATEADD('DAY', 5, CURRENT_DATE), 'HIGH', false, CURRENT_TIMESTAMP);

INSERT INTO skills (skill_id, user_id, skill_name, category, proficiency, hours_practiced, projects_count, started_date, last_used, created_at) VALUES
(1, 1, 'Java', 'LANGUAGE', 'INTERMEDIATE', 150.0, 5, DATEADD('DAY', -180, CURRENT_DATE), CURRENT_DATE, CURRENT_TIMESTAMP),
(2, 1, 'Spring Boot', 'FRAMEWORK', 'INTERMEDIATE', 80.0, 3, DATEADD('DAY', -90, CURRENT_DATE), CURRENT_DATE, CURRENT_TIMESTAMP),
(3, 1, 'JavaScript', 'LANGUAGE', 'INTERMEDIATE', 100.0, 4, DATEADD('DAY', -150, CURRENT_DATE), CURRENT_DATE, CURRENT_TIMESTAMP),
(4, 1, 'MySQL', 'DATABASE', 'INTERMEDIATE', 60.0, 4, DATEADD('DAY', -120, CURRENT_DATE), CURRENT_DATE, CURRENT_TIMESTAMP),
(5, 1, 'React', 'FRAMEWORK', 'BEGINNER', 30.0, 1, DATEADD('DAY', -30, CURRENT_DATE), DATEADD('DAY', -2, CURRENT_DATE), CURRENT_TIMESTAMP);

-- Reset Sequences to prevent primary key violations
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 2;
ALTER TABLE daily_logs ALTER COLUMN log_id RESTART WITH 8;
ALTER TABLE dsa_problems ALTER COLUMN problem_id RESTART WITH 8;
ALTER TABLE goals ALTER COLUMN goal_id RESTART WITH 4;
ALTER TABLE skills ALTER COLUMN skill_id RESTART WITH 6;
