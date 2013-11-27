@echo off

title WSTest HTTP XML API

cd /d %0\..

set CLASSPATH=./bin
set CLASSPATH=%CLASSPATH%;./lib/*
set CLASSPATH=%CLASSPATH%;./conf

set JAVA_OPTS=-Xms32M -Xmx128M -server
set JAVA_OPTS=%JAVA_OPTS% -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false -Dfile.encoding=UTF-8


@echo on

java %JAVA_OPTS% -cp "%CLASSPATH%" wstest.srv.GrizzlyWSTestSrv
