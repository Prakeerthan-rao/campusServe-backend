-- DEPARTMENTS
INSERT INTO department (id, name, description)
VALUES
  (1, 'IT',             'Handles all IT-related issues'),
  (2, 'EDUCATION',      'Handles fee and scholarship queries'),
  (3, 'Library',        'Handles library-related requests'),
  (4, 'Administration', 'General administrative support'),
  (5, 'Hostel',         'Hostel and accommodation support'),
  (6, 'CLEANING',       'Responsible for cleaning'),
  (7, 'TRANSPORT',      'Handles transport'),
  (8, 'SPORTS',         'Handles sports issues')
ON CONFLICT (id) DO NOTHING;

-- ADMIN (password = admin123)
INSERT INTO admin (id, username, password, email, full_name, role)
VALUES (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFiy', 'admin@campus.edu', 'System Admin', 'ADMIN')
ON CONFLICT (id) DO NOTHING;

-- STAFF (password = staff123)
INSERT INTO staff (id, username, password, email, full_name, department_id, role)
VALUES
  (1, 'it_staff',      '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFiy', 'it@campus.edu',      'IT Staff Member', 1, 'STAFF'),
  (2, 'finance_staff', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFiy', 'finance@campus.edu', 'Finance Staff',   2, 'STAFF'),
  (3, 'lib_staff',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFiy', 'lib@campus.edu',     'Library Staff',   3, 'STAFF')
ON CONFLICT (id) DO NOTHING;

-- STUDENTS (password = student123)
INSERT INTO student (id, username, password, email, full_name, roll_number, department_id, year)
VALUES
  (1, 'john_doe',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFiy', 'john@campus.edu', 'John Doe',   'CS2021001', 1, 3),
  (2, 'jane_smith', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFiy', 'jane@campus.edu', 'Jane Smith', 'EC2021002', 1, 2),
  (3, 'bob_wilson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lFiy', 'bob@campus.edu',  'Bob Wilson', 'ME2022001', 1, 1)
ON CONFLICT (id) DO NOTHING;
