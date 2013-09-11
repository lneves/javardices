@echo off

title WSTest HTTP XML API

cd /d %~dp0

set CLASSPATH=conf;lib\*

set JAVA_OPTS=-server -XX:+UseNUMA -XX:+UseParallelGC -Xms256M -Xmx512M
set JAVA_OPTS=%JAVA_OPTS% -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false
set JAVA_OPTS=%JAVA_OPTS% -Djava.awt.headless=true -Dfile.encoding=UTF-8


@echo on

java  %JAVA_OPTS% -cp "%CLASSPATH%" org.caudexorigo.wstest.srv.HttpEndpoint -p 9080