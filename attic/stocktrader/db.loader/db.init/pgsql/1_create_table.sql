
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
