-- Clean pharmacy-app database, keep only admin user (etteyebadem1@gmail.com)

-- 1. Delete all reservation items first (child table)
DELETE FROM reservation_item;

-- 2. Delete all reservations
DELETE FROM reservation;

-- 3. Delete all stock records
DELETE FROM stock;

-- 4. Delete all notifications
DELETE FROM notification;

-- 5. Delete all password reset tokens
DELETE FROM password_reset_token;

-- 6. Delete all client records
DELETE FROM client;

-- 7. Delete all pharmacy records
DELETE FROM pharmacy;

-- 8. Delete all user_role records except for the admin user
DELETE FROM user_role WHERE user_id NOT IN (SELECT id FROM user_account WHERE email = 'etteyebadem1@gmail.com');

-- 9. Delete all user_accounts except the admin
DELETE FROM user_account WHERE email != 'etteyebadem1@gmail.com';

-- Optional: Reset drug table if you want fresh drugs
-- DELETE FROM drug;

-- Optional: Reset auto-increment counters (uncomment if needed)
-- ALTER TABLE user_account AUTO_INCREMENT = 1;
-- ALTER TABLE client AUTO_INCREMENT = 1;
-- ALTER TABLE pharmacy AUTO_INCREMENT = 1;
-- ALTER TABLE reservation AUTO_INCREMENT = 1;
-- ALTER TABLE drug AUTO_INCREMENT = 1;
-- ALTER TABLE stock AUTO_INCREMENT = 1;

-- Verify remaining data
-- SELECT * FROM user_account;
-- SELECT * FROM user_role;