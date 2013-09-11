DROP DATABASE IF EXISTS stocktrader;

CREATE DATABASE stocktrader WITH OWNER = postgres ENCODING = 'UTF8' TABLESPACE = pg_default LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' CONNECTION LIMIT = -1;

CREATE USER trade WITH PASSWORD 'bengals#1';

CREATE OR REPLACE PROCEDURAL LANGUAGE plpgsql;

DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS accountprofile CASCADE;
DROP TABLE IF EXISTS holding CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS quote CASCADE;


CREATE TABLE account
(
	accountid serial NOT NULL,
	profile_userid varchar(50),			    
	balance numeric(12,2),
	openbalance numeric(12,2),
	creationdate timestamp with time zone,
	lastlogin timestamp with time zone,
	logincount integer NOT NULL,   
	logoutcount integer NOT NULL
);


CREATE TABLE accountprofile
(
    userid varchar(50) NOT NULL,
    email varchar(250),
    fullname varchar(250),
    password varchar(50),
	salt varchar(20),
    address varchar(250),    
    creditcard varchar(250)
);


CREATE TABLE holding
(
    holdingid serial NOT NULL,
    account_accountid integer,
	quote_symbol varchar(10),
    purchaseprice numeric(12,2),
    quantity double precision NOT NULL,
    purchasedate timestamp with time zone
);


CREATE TABLE orders
(
	orderid serial NOT NULL,
	account_accountid integer,
	quote_symbol varchar(10),
	holding_holdingid integer,
	ordertype varchar(250),
	orderstatus varchar(250),
	orderfee numeric(12,2),
	price numeric(12,2),
	quantity double precision NOT NULL,    
	opendate timestamp with time zone,
	completiondate timestamp with time zone
);


CREATE TABLE quote
(
	symbol varchar(10) NOT NULL,
	companyname varchar(250),
	price numeric(12,2),
	open1 numeric(12,2),
	volume double precision NOT NULL,	
	low numeric(12,2),
	high numeric(12,2),    
	change1 double precision NOT NULL
);

GRANT ALL ON ALL TABLES IN SCHEMA PUBLIC TO trade;
GRANT ALL ON ALL SEQUENCES IN SCHEMA PUBLIC TO trade;

\copy account FROM '../../data.gen/out/account.txt' DELIMITER E'\t'
\copy accountprofile FROM '../../data.gen/out/account_profile.txt' DELIMITER E'\t'
\copy holding FROM '../../data.gen/out/holding.txt' DELIMITER E'\t'
\copy orders FROM '../../data.gen/out/order.txt' DELIMITER E'\t'
\copy quote FROM '../../data.gen/out/quote.txt' DELIMITER E'\t'


SELECT pg_catalog.setval(pg_get_serial_sequence('account', 'accountid'), (SELECT MAX(accountid) FROM account)+1);
SELECT pg_catalog.setval(pg_get_serial_sequence('holding', 'holdingid'), (SELECT MAX(holdingid) FROM holding)+1);
SELECT pg_catalog.setval(pg_get_serial_sequence('orders', 'orderid'), (SELECT MAX(orderid) FROM orders)+1);

ALTER TABLE ONLY account ADD CONSTRAINT pk_account PRIMARY KEY (accountid);
ALTER TABLE ONLY accountprofile ADD CONSTRAINT pk_accountprofile PRIMARY KEY (userid);
ALTER TABLE ONLY holding ADD CONSTRAINT pk_holding PRIMARY KEY (holdingid);
ALTER TABLE ONLY orders ADD CONSTRAINT pk_order PRIMARY KEY (orderid);
ALTER TABLE ONLY quote ADD CONSTRAINT pk_quote PRIMARY KEY (symbol);


CREATE INDEX account_profile_userid_idx ON account (profile_userid);
CREATE INDEX holding_account_accountid_holdingid_quote_symbol_idx ON holding (account_accountid, holdingid, quote_symbol);
CREATE INDEX holding_account_accountid_idx ON holding (account_accountid);
CREATE INDEX orders_account_accountid_idx ON orders (account_accountid);
CREATE INDEX orders_account_accountid_orderid_idx ON orders (account_accountid, orderid);
CREATE INDEX orders_orderstatus_account_accountid_idx ON orders (orderstatus, account_accountid);
CREATE INDEX quote_open1_idx ON quote (open1);
CREATE INDEX quote_price_idx ON quote (price);

ALTER TABLE account ADD CONSTRAINT fk_a1 FOREIGN KEY (profile_userid) REFERENCES accountprofile (userid) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE holding ADD CONSTRAINT fk_h1 FOREIGN KEY (account_accountid) REFERENCES account (accountid) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE holding ADD CONSTRAINT fk_h2 FOREIGN KEY (quote_symbol) REFERENCES quote (symbol) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE orders ADD CONSTRAINT fk_o1 FOREIGN KEY (quote_symbol) REFERENCES quote (symbol) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE orders ADD CONSTRAINT fk_o2 FOREIGN KEY (account_accountid) REFERENCES account (accountid) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE orders ADD CONSTRAINT fk_o3 FOREIGN KEY (holding_holdingid) REFERENCES holding (holdingid) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;

ANALYZE account;
ANALYZE accountprofile;
ANALYZE orders;
ANALYZE holding;
ANALYZE quote;

-- CLUSTER account USING pk_account;
-- CLUSTER accountprofile USING pk_accountprofile;
-- CLUSTER holding USING pk_holding;
-- CLUSTER orders USING pk_order;
-- CLUSTER quote USING pk_quote;
-- ALTER DATABASE stocktrader OWNER TO trade;
