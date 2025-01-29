INSERT INTO users (user_id, mail, password, role, enabled, budget, question, answer, phone_number)
VALUES
    (1, 'testuser1@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 1000.00, 'Was ist deine Lieblingsfarbe?', 'Rot', '+491234567890'),
    (2, 'testuser2@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 1500.50, 'Was ist der Mädchenname deiner Mutter?', 'Müller', '+491234567890'),
    (3, 'testuser3@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 2000.75, 'Was ist der Name deines ersten Haustieres?', 'Bello', '+491234567890'),
    (4, 'testuser4@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 2500.25, 'Was ist deine Lieblingsfarbe?', 'Rot', '+491234567890'),
    (5, 'testuser5@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 3000.00, 'Was ist der Mädchenname deiner Mutter?', 'Schneider', '+491234567890'),
    (6, 'testuser6@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 3500.50, 'Was ist der Name deines ersten Haustieres?', 'Kitty', '+491234567890'),
    (7, 'testuser7@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 4000.75, 'Was ist deine Lieblingsfarbe?', 'Blau', '+491234567890'),
    (8, 'testuser8@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 4500.25, 'Was ist der Mädchenname deiner Mutter?', 'Fischer', '+491234567890'),
    (9, 'testuser9@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 5000.00, 'Was ist der Name deines ersten Haustieres?', 'Simba', '+491234567890'),
    (10, 'testuser10@example.com', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 5500.50, 'Was ist deine Lieblingsfarbe?', 'Grün', '+491234567890'),
    (11, 'marc.grunwald@stud.fra-uas.de', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 5500.50, 'Was ist deine Lieblingsfarbe?', 'Rot', '+491234567890'),
    (12, 'unkart@stud.fra-uas.de', '$2a$10$NPENOBdGKCmQPN8b8ejORu4s6PTuZpaQBNbhI1Gt4S.av4rpaG032', 'USER', true, 5500.50, 'Was ist deine Lieblingsfarbe?', 'Rot', '+491234567890');


INSERT INTO login_events (user_id, mail, login_time, ip_address, location, browser, browser_version, operating_system, status)
VALUES
   (1, 'testuser1@example.com', '2025-01-14 10:14:16.998657', '127.0.0.1', 'Frankfurt', 'TestBrowser', 'TestBVersion', 'TestOpSystem', 'Success'),
   (1, 'testuser1@example.com', '2025-01-14 10:14:16.998657', '127.0.0.1', 'Frankfurt', 'TestBrowser', 'TestBVersion', 'TestOpSystem', 'Success'),
   (12, 'unkart@stud.fra-uas.de', '2025-01-28 17:56:54.285968', '127.0.0.1', 'Frankfurt', 'Chrome', '131.0.0.0', 'Windows NT 10.0; Win64; x64', 'Success'),
   (12, 'unkart@stud.fra-uas.de', '2025-01-28 17:56:54.285968', '127.0.0.1', 'Frankfurt', 'Chrome', '131.0.0.0', 'Windows NT 10.0; Win64; x64', 'Success'),
   (12, 'unkart@stud.fra-uas.de', '2025-01-28 17:56:54.285968', '127.0.0.1', 'Frankfurt', 'Chrome', '131.0.0.0', 'Windows NT 10.0; Win64; x64', 'Success'),
   (12, 'unkart@stud.fra-uas.de', '2025-01-28 17:56:54.285968', '127.0.0.1', 'Frankfurt', 'Chrome', '131.0.0.0', 'Windows NT 10.0; Win64; x64', 'Success'),
   (12, 'unkart@stud.fra-uas.de', '2025-01-28 17:56:54.285968', '127.0.0.1', 'Frankfurt', 'Chrome', '131.0.0.0', 'Windows NT 10.0; Win64; x64', 'Success');


INSERT INTO transactions (sender_id, recipient_id, amount, timestamp)
VALUES
    (1, 2, 100.00, '2025-01-14 10:00:00'),
    (2, 3, 200.50, '2025-01-14 11:00:00'),
    (3, 4, 300.75, '2025-01-14 12:00:00'),
    (4, 5, 400.25, '2025-01-14 13:00:00'),
    (5, 6, 500.00, '2025-01-14 14:00:00'),
    (6, 7, 600.50, '2025-01-14 15:00:00'),
    (7, 8, 700.75, '2025-01-14 16:00:00'),
    (8, 9, 800.25, '2025-01-14 17:00:00'),
    (9, 10, 900.50, '2025-01-14 18:00:00'),
    (10, 1, 1000.75, '2025-01-14 19:00:00');
