USE  stocktrader
GO


DECLARE @Sql NVARCHAR(500) DECLARE @Cursor CURSOR
SET @Cursor = CURSOR FAST_FORWARD FOR

SELECT DISTINCT sql = 'ALTER TABLE ' + TABLE_SCHEMA + '.[' + TABLE_NAME + '] DROP CONSTRAINT [' + CONSTRAINT_NAME + ']' FROM information_schema.table_constraints WHERE CONSTRAINT_TYPE = 'FOREIGN KEY'

OPEN @Cursor FETCH NEXT FROM @Cursor INTO @Sql

WHILE (@@FETCH_STATUS = 0)

BEGIN

Exec SP_EXECUTESQL @Sql

FETCH NEXT FROM @Cursor INTO @Sql

END

CLOSE @Cursor DEALLOCATE @Cursor
GO

ALTER TABLE account ADD CONSTRAINT fk_a1 FOREIGN KEY (profile_userid) REFERENCES accountprofile (userid);
ALTER TABLE holding ADD CONSTRAINT fk_h1 FOREIGN KEY (account_accountid) REFERENCES account (accountid);
ALTER TABLE holding ADD CONSTRAINT fk_h2 FOREIGN KEY (quote_symbol) REFERENCES quote (symbol);
ALTER TABLE orders ADD CONSTRAINT fk_o1 FOREIGN KEY (quote_symbol) REFERENCES quote (symbol);
ALTER TABLE orders ADD CONSTRAINT fk_o2 FOREIGN KEY (account_accountid) REFERENCES account (accountid);
ALTER TABLE orders ADD CONSTRAINT fk_o3 FOREIGN KEY (holding_holdingid) REFERENCES holding (holdingid) ON DELETE CASCADE ON UPDATE NO ACTION;

GO
