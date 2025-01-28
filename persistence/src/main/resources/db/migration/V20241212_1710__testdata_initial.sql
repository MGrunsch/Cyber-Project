INSERT INTO users (user_id, mail, password, role, enabled, budget)
VALUES
    (1, 'testuser1@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 1000.00),
    (2, 'testuser2@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 1500.50),
    (3, 'testuser3@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 2000.75),
    (4, 'testuser4@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 2500.25),
    (5, 'testuser5@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 3000.00),
    (6, 'testuser6@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 3500.50),
    (7, 'testuser7@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 4000.75),
    (8, 'testuser8@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 4500.25),
    (9, 'testuser9@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 5000.00),
    (10, 'testuser10@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 5500.50),
    (11, 'marc.grunwald@stud.fra-uas.de', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 5500.50),
    (12, 'unkart@stud.fra-uas.de', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 5500.50);


INSERT INTO login_events (user_id, mail, login_time, ip_address, location, browser, browser_version, operating_system, status)
VALUES
   (1, 'testuser1@example.com', '2025-01-14 10:14:16.998657', '127.0.0.1', 'Frankfurt', 'TestBrowser', 'TestBVersion', 'TestOpSystem', 'Success'),
   (1, 'testuser1@example.com', '2025-01-14 10:14:16.998657', '127.0.0.1', 'Frankfurt', 'TestBrowser', 'TestBVersion', 'TestOpSystem', 'Success');