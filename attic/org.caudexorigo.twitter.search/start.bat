@echo off

title Netty Web Socket Server

cd /d %0\..

set CLASSPATH=./conf;./lib/*

set JAVA_OPTS=-Xms16M -Xmx32M
set JAVA_OPTS=%JAVA_OPTS% -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false -Dfile.encoding=UTF-8

@echo on

java %JAVA_OPTS% -cp "%CLASSPATH%" org.caudexorigo.http.netty.DefaultServer --port 8080 --root-directory "wwwroot"
