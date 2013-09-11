USE master
GO

IF EXISTS(SELECT * FROM sys.databases WHERE name='stocktrader')
DROP DATABASE stocktrader
GO

CREATE DATABASE stocktrader
GO

CREATE LOGIN trade  WITH PASSWORD = 'bengals#1'
GO

USE stocktrader
GO

CREATE USER trade FOR LOGIN trade;
GO

EXEC sp_addrolemember 'db_owner','trade'
GO
