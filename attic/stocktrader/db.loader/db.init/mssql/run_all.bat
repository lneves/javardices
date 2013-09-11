
cd /d %~dp0

@echo off

set DB_SERVER=localhost
set DB_AUTH=TRUSTED
::set DB_AUTH=-U your_user -P secret
::set UNIX_LB=-r 0x0a

set BCP_AUTH=-T
if "%DB_AUTH%" NEQ "TRUSTED" set BCP_AUTH=%DB_AUTH%


set SQLCMD_AUTH=-E
if "%DB_AUTH%" NEQ "TRUSTED" set SQLCMD_AUTH=%DB_AUTH%

@echo on

sqlcmd -H %DB_SERVER% %SQLCMD_AUTH% -i 0_create_database.sql
sqlcmd -H %DB_SERVER% %SQLCMD_AUTH% -i 1_create_table.sql

bcp account in "..\..\data.gen\out\account.txt" -S %DB_SERVER% -d stocktrader -c -E %BCP_AUTH% %UNIX_LB%
bcp accountprofile in "..\..\data.gen\out\account_profile.txt" -S %DB_SERVER% -d stocktrader -c -T %UNIX_LB%
bcp holding in "..\..\data.gen\out\holding.txt" -S %DB_SERVER% -d stocktrader -c -E %BCP_AUTH% %UNIX_LB%
bcp orders in "..\..\data.gen\out\order.txt" -S %DB_SERVER% -d stocktrader -c -E %BCP_AUTH% %UNIX_LB%
bcp quote in "..\..\data.gen\out\quote.txt" -S %DB_SERVER% -d stocktrader -c %BCP_AUTH% %UNIX_LB%

sqlcmd -H %DB_SERVER% %SQLCMD_AUTH% -i 3_create_keys.sql
sqlcmd -H %DB_SERVER% %SQLCMD_AUTH% -i 4_create_index.sql
sqlcmd -H %DB_SERVER% %SQLCMD_AUTH% -i 5_create_fk.sql
sqlcmd -H %DB_SERVER% %SQLCMD_AUTH% -i 6_analyze_table.sql


@echo off
for %%x in (%cmdcmdline%) do if %%~x==/c set DOUBLECLICKED=1
if defined DOUBLECLICKED pause

