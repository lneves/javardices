@echo off

title mssql_load_driver

chdir %CD%
set CLASSPATH=./etc/mssql
set CLASSPATH=%CLASSPATH%;./lib/*

set JAVA_OPTS= -Xms64M -Xmx512M
set JAVA_OPTS=%JAVA_OPTS% -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false -Dfile.encoding=UTF-8

rem ********* run test ***********

@echo on

java %JAVA_OPTS% -cp "%CLASSPATH%" db.tpce.driver.LoadDriver %*

pause

