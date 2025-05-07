-- Insert Teachers with credentials (password is 'password' encoded with SHA256)
INSERT INTO teachers (name, last_name, department, email, password) VALUES
('John', 'Doe', 'Computer Science', 'john.doe@example.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg='),
('Jane', 'Smith', 'Mathematics', 'jane.smith@example.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=');

-- Insert Courses
INSERT INTO courses (name, course_no, teacher_id) VALUES
('Introduction to Programming', 'CS101', 1),
('Data Structures', 'CS102', 1),
('Calculus I', 'MATH101', 2),
('Advanced Programming', 'CS201', 1),
('Algorithms', 'CS301', 1),
('Database Systems', 'CS401', 1),
('Web Development', 'CS501', 1),
('Computer Networks', 'CS601', 1),
('Calculus II', 'MATH201', 2),
('Linear Algebra', 'MATH301', 2),
('Discrete Mathematics', 'MATH401', 2),
('Statistics', 'MATH501', 2),
('Number Theory', 'MATH601', 2);

-- Insert 10 New Courses


-- Insert Students with credentials (password is 'password' encoded with SHA256)
INSERT INTO students (name, last_name, email, student_no, enrollment_date, password, adviser_id) VALUES
('Alice', 'Johnson', 'alice@example.com', '2023001', '2023-09-01', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', 1),
('Bob', 'Wilson', 'bob@example.com', '2023002', '2023-09-01', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', 2);

-- Enroll Students in Courses (Active courses)
INSERT INTO student_courses (student_id, course_id, status) VALUES
(1, 1, 'ACTIVE'),
(1, 2, 'ACTIVE'),
(2, 1, 'ACTIVE'),
(1, 4, 'ACTIVE'),  -- Alice in Advanced Programming
(1, 5, 'ACTIVE'),  -- Alice in Algorithms
(1, 6, 'ACTIVE'),  -- Alice in Database Systems
(1, 7, 'ACTIVE'),  -- Alice in Web Development
(1, 8, 'ACTIVE'),  -- Alice in Computer Networks
(2, 9, 'ACTIVE'),   -- Bob in Calculus II
(2, 10, 'ACTIVE'),  -- Bob in Linear Algebra
(2, 11, 'ACTIVE'),  -- Bob in Discrete Mathematics
(2, 12, 'ACTIVE'),  -- Bob in Statistics
(2, 13, 'ACTIVE'),  -- Bob in Number Theory
(1, 9, 'PENDING'),  -- Alice wants to take Calculus II (Jane's course)
(1, 10, 'PENDING'), -- Alice wants to take Linear Algebra (Jane's course)
(2, 3, 'PENDING'),
(2, 4, 'PENDING'),  -- Bob wants to take Advanced Programming (John's course)
(2, 5, 'PENDING');  -- Bob wants to take Algorithms (John's course)
