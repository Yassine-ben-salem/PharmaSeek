-- =====================================================
-- CLEAR ALL DATA (KEEP TABLES & ROLES)
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE password_reset_token;
TRUNCATE TABLE notification;
TRUNCATE TABLE reservation_item;
TRUNCATE TABLE reservation;
TRUNCATE TABLE stock;
TRUNCATE TABLE client;
TRUNCATE TABLE pharmacy;
TRUNCATE TABLE user_role;
TRUNCATE TABLE user_account;
TRUNCATE TABLE drug;

SET FOREIGN_KEY_CHECKS = 1;

-- Verify data is cleared
SELECT 'user_account' as tbl, COUNT(*) as cnt FROM user_account
UNION ALL SELECT 'client', COUNT(*) FROM client
UNION ALL SELECT 'pharmacy', COUNT(*) FROM pharmacy
UNION ALL SELECT 'drug', COUNT(*) FROM drug
UNION ALL SELECT 'reservation', COUNT(*) FROM reservation
UNION ALL SELECT 'stock', COUNT(*) FROM stock
UNION ALL SELECT 'notification', COUNT(*) FROM notification;