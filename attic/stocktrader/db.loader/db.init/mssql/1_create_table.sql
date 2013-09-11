
USE stocktrader;


SET ANSI_NULLS ON;
SET ANSI_NULL_DFLT_OFF ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET ARITHABORT ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET QUOTED_IDENTIFIER ON;
SET NUMERIC_ROUNDABORT OFF;



DECLARE @Sql NVARCHAR(500) DECLARE @Cursor CURSOR
SET @Cursor = CURSOR FAST_FORWARD FOR

SELECT DISTINCT sql = 'ALTER TABLE [' + tc2.TABLE_NAME + '] DROP [' + rc1.CONSTRAINT_NAME + ']'
FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS rc1
LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc2 ON tc2.CONSTRAINT_NAME =rc1.CONSTRAINT_NAME

OPEN @Cursor FETCH NEXT FROM @Cursor INTO @Sql

WHILE (@@FETCH_STATUS = 0)

BEGIN

Exec SP_EXECUTESQL @Sql

FETCH NEXT FROM @Cursor INTO @Sql

END

CLOSE @Cursor DEALLOCATE @Cursor
GO

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'account')
DROP TABLE account;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'accountprofile')
DROP TABLE accountprofile;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'holding')
DROP TABLE holding;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'orders')
DROP TABLE orders;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'quote')
DROP TABLE quote;

GO


CREATE TABLE account
(
	accountid int identity(0,1) NOT NULL,
	profile_userid varchar(50),			    
	balance decimal(12,2),
	openbalance decimal(12,2),
	creationdate datetime,
	lastlogin datetime,
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
    holdingid int identity(0,1) NOT NULL,
    account_accountid integer,
	quote_symbol varchar(10),
    purchaseprice decimal(12,2),
    quantity float NOT NULL,
    purchasedate datetime
);


CREATE TABLE orders
(
	orderid int identity(0,1) NOT NULL,
	account_accountid integer,
	quote_symbol varchar(10),
	holding_holdingid integer,
	ordertype varchar(250),
	orderstatus varchar(250),
	orderfee decimal(12,2),
	price decimal(12,2),
	quantity float NOT NULL,    
	opendate datetime,
	completiondate datetime
);


CREATE TABLE quote
(
	symbol varchar(10) NOT NULL,
	companyname varchar(250),
	price decimal(12,2),
	open1 decimal(12,2),
	volume float NOT NULL,	
	low decimal(12,2),
	high decimal(12,2),    
	change1 float NOT NULL
);

GO
