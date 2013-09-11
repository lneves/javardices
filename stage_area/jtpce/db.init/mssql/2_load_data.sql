
USE master
GO

ALTER DATABASE tpce SET RECOVERY BULK_LOGGED
GO

ALTER DATABASE tpce SET TORN_PAGE_DETECTION OFF
GO

ALTER DATABASE tpce SET PAGE_VERIFY NONE
GO

ALTER DATABASE tpce SET ALLOW_SNAPSHOT_ISOLATION ON
GO


USE tpce
GO

CHECKPOINT
GO

USE  tpce
GO

SET NOCOUNT ON
GO

PRINT 'bulk insert charge'
GO
BULK INSERT charge FROM 'c:\path\To\Charge.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 5, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert commission_rate'
BULK INSERT commission_rate FROM 'c:\path\To\CommissionRate.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 240, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert exchange'
GO
BULK INSERT exchange FROM 'c:\path\To\Exchange.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 4, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert industry'
GO
BULK INSERT industry FROM 'c:\path\To\Industry.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 102, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert sector'
GO
BULK INSERT sector FROM 'c:\path\To\Sector.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 12, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert status_type'
GO
BULK INSERT status_type FROM 'c:\path\To\Statustype.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 5, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert taxrate'
GO
BULK INSERT taxrate FROM 'c:\path\To\Taxrate.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 320, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert trade_type'
GO
BULK INSERT trade_type FROM 'c:\path\To\TradeType.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 5, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert zip_code'
GO
BULK INSERT zip_code FROM 'c:\path\To\ZipCode.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 14741, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert account_permission'
GO
BULK INSERT account_permission FROM 'c:\path\To\AccountPermission.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 195750, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert address'
GO
BULK INSERT address FROM 'c:\path\To\Address.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 40504, tablock)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert broker'
GO
BULK INSERT broker FROM 'c:\path\To\Broker.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 270, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert company'
GO
BULK INSERT company FROM 'c:\path\To\Company.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 13500, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert company_competitor'
GO
BULK INSERT company_competitor FROM 'c:\path\To\CompanyCompetitor.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 40500, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert customer'
GO
BULK INSERT customer FROM 'c:\path\To\Customer.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 27000, tablock)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert customer_account'
GO
BULK INSERT customer_account FROM 'c:\path\To\CustomerAccount.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 135000, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert customer_taxrate'
GO
BULK INSERT customer_taxrate FROM 'c:\path\To\CustomerTaxrate.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 54000, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert daily_market'
GO
BULK INSERT daily_market FROM 'c:\path\To\DailyMarket.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 24135975, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert financial'
GO
BULK INSERT financial FROM 'c:\path\To\Financial.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 270000, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert last_trade'
GO
BULK INSERT last_trade FROM 'c:\path\To\LastTrade.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 18495, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert news_item_temp'
GO
BULK INSERT news_item FROM 'c:\path\To\NewsItem.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 27000, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert news_xref'
GO
BULK INSERT news_xref FROM 'c:\path\To\NewsXRef.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 27000, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert security'
GO
BULK INSERT security FROM 'c:\path\To\Security.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 18495, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert watch_item'
GO
BULK INSERT watch_item FROM 'c:\path\To\WatchItem.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 2727000, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert watch_list'
GO
BULK INSERT watch_list FROM 'c:\path\To\WatchList.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 27000, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert cash_transaction'
GO
BULK INSERT cash_transaction FROM 'c:\path\To\CashTransaction.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 429701760, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert holding'
GO
BULK INSERT holding FROM 'c:\path\To\Holding.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 25194240, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert holding_history'
GO
BULK INSERT holding_history FROM 'c:\path\To\HoldingHistory.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 627056640, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert holding_summary'
GO
BULK INSERT holding_summary FROM 'c:\path\To\HoldingSummary.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 1348650, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert settlement'
GO
BULK INSERT settlement FROM 'c:\path\To\Settlement.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 466560000, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert trade'
GO
BULK INSERT trade FROM 'c:\path\To\Trade.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 466560000, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert trade_history'
GO
BULK INSERT trade_history FROM 'c:\path\To\TradeHistory.txt' WITH ( FIELDTERMINATOR = '|', ROWS_PER_BATCH = 1124409600, TABLOCK, KEEPNULLS)
GO
SELECT 'rows inserted:', @@rowcount
GO

PRINT 'bulk insert done!'
GO

USE master;
GO

EXEC sp_dboption tpce,'select into/bulkcopy',false
EXEC sp_dboption tpce,'trunc. log on chkpt.',false
EXEC sp_dboption tpce,'torn page detection',false
EXEC sp_dboption tpce,'Auto Update Statistics',false
GO

ALTER DATABASE tpce SET RECOVERY FULL
GO

ALTER DATABASE tpce SET ALLOW_SNAPSHOT_ISOLATION ON
GO

RECONFIGURE WITH OVERRIDE
GO

USE tpce
GO

CHECKPOINT
GO
