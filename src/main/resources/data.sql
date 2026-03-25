INSERT IGNORE INTO department (id, name, description) VALUES
(1, 'IT Support',     'Handles all IT-related issues'),
(2, 'Finance',        'Handles fee and scholarship queries'),
(3, 'Library',        'Handles library-related requests'),
(4, 'Administration', 'General administrative support'),
(5, 'Hostel',         'Hostel and accommodation support');

-- password = admin123 (BCrypt)
INSERT IGNORE INTO admin (id, username, password, email, full_name)
VALUES (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@campus.edu', 'System Admin');

-- password = staff123 (BCrypt)
INSERT IGNORE INTO staff (id, username, password, email, full_name, department_id, role) VALUES
(1, 'it_staff',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'it@campus.edu',      'IT Staff Member', 1, 'STAFF'),
(2, 'finance_staff', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'finance@campus.edu', 'Finance Staff',   2, 'STAFF'),
(3, 'lib_staff',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'lib@campus.edu',     'Library Staff',   3, 'STAFF');

-- password = student123 (BCrypt)
INSERT IGNORE INTO student (id, username, password, email, full_name, roll_number, department_id, year) VALUES
(1, 'john_doe',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'john@campus.edu', 'John Doe',   'CS2021001', 1, 3),
(2, 'jane_smith', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'jane@campus.edu', 'Jane Smith', 'EC2021002', 1, 2),
(3, 'bob_wilson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'bob@campus.edu',  'Bob Wilson', 'ME2022001', 1, 1);
