-- Insert a pending course enrollment for Jane Smith to approve
INSERT INTO student_courses (student_id, course_id, status)
SELECT s.id, 3, 'PENDING'
FROM students s
WHERE s.email = 'john.doe@example.com'
AND NOT EXISTS (
    SELECT 1 FROM student_courses sc 
    WHERE sc.student_id = s.id AND sc.course_id = 3
);

-- Create pending course enrollments for advisees
-- Bob (ID 2) is advised by Jane (ID 2)
INSERT INTO student_courses (student_id, course_id, status) 
VALUES (2, 2, 'PENDING');  -- Course ID 2 is Data Structures

-- Alice (ID 1) is advised by John (ID 1)
INSERT INTO student_courses (student_id, course_id, status) 
VALUES (1, 3, 'PENDING');  -- Course ID 3 is Calculus I 