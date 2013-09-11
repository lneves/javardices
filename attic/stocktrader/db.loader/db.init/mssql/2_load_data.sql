
USE master
GO

ALTER DATABASE stocktrader SET RECOVERY BULK_LOGGED
GO

ALTER DATABASE stocktrader SET TORN_PAGE_DETECTION OFF
GO

ALTER DATABASE stocktrader SET PAGE_VERIFY NONE
GO

ALTER DATABASE stocktrader SET ALLOW_SNAPSHOT_ISOLATION ON
GO


USE stocktrader
GO

CHECKPOINT
GO

USE  stocktrader
GO

SET NOCOUNT ON
GO



PRINT 'bulk insert account'
GO
BULK INSERT account FROM 'c:\path\to\data.files\out\account.txt' WITH ( FIELDTERMINATOR = '\t', ROWTERMINATOR = '0x0a', TABLOCK, KEEPNULLS, KEEPIDENTITY)
GO
SELECT 'rows inserted:', @@rowcount
GO


PRINT 'bulk insert accountprofile'
GO
BULK INSERT accountprofile FROM 'c:\path\to\data.files\out\account_profile.txt' WITH ( FIELDTERMINATOR = '\t', ROWTERMINATOR = '0x0a', TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert holding'
GO
BULK INSERT holding FROM 'c:\path\to\data.files\out\holding.txt' WITH ( FIELDTERMINATOR = '\t', ROWTERMINATOR = '0x0a', TABLOCK, KEEPNULLS, KEEPIDENTITY)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert orders'
GO
BULK INSERT orders FROM 'c:\path\to\data.files\out\order.txt' WITH ( FIELDTERMINATOR = '\t', ROWTERMINATOR = '0x0a', TABLOCK, KEEPNULLS, KEEPIDENTITY)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert quote'
GO
BULK INSERT quote FROM 'c:\path\to\data.files\out\quote.txt' WITH ( FIELDTERMINATOR = '\t', ROWTERMINATOR = '0x0a', TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO


USE master;
GO

EXEC sp_dboption stocktrader,'select into/bulkcopy',false
EXEC sp_dboption stocktrader,'trunc. log on chkpt.',false
EXEC sp_dboption stocktrader,'torn page detection',false
EXEC sp_dboption stocktrader,'Auto Update Statistics',false
GO

ALTER DATABASE stocktrader SET RECOVERY FULL
GO

ALTER DATABASE stocktrader SET ALLOW_SNAPSHOT_ISOLATION ON
GO

RECONFIGURE WITH OVERRIDE
GO

USE stocktrader
GO

CHECKPOINT
GO
