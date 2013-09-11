
\copy account FROM '../../data.gen/out/account.txt' DELIMITER E'\t'
\copy accountprofile FROM '../../data.gen/out/account_profile.txt' DELIMITER E'\t'
\copy holding FROM '../../data.gen/out/holding.txt' DELIMITER E'\t'
\copy orders FROM '../../data.gen/out/order.txt' DELIMITER E'\t'
\copy quote FROM '../../data.gen/out/quote.txt' DELIMITER E'\t'


SELECT pg_catalog.setval(pg_get_serial_sequence('account', 'accountid'), (SELECT MAX(accountid) FROM account)+1);
SELECT pg_catalog.setval(pg_get_serial_sequence('holding', 'holdingid'), (SELECT MAX(holdingid) FROM holding)+1);
SELECT pg_catalog.setval(pg_get_serial_sequence('orders', 'orderid'), (SELECT MAX(orderid) FROM orders)+1);
