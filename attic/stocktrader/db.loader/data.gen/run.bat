@echo off

cd /d %~dp0

set CLASSPATH=./lib/*

set JAVA_OPTS= -server -Xms32M -Xmx128M
set JAVA_OPTS=%JAVA_OPTS% -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false -Dfile.encoding=UTF-8



@echo on
java %JAVA_OPTS% -cp "%CLASSPATH%" trade.datagen.DataGenerator --input-path .\in --output-path .\out --accounts=5000 --quotes=1000 --holdings=5



@echo off
for %%x in (%cmdcmdline%) do if %%~x==/c set DOUBLECLICKED=1
if defined DOUBLECLICKED pause
