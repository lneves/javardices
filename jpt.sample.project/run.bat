@echo off

title jpt_sample

chdir %CD%
set CLASSPATH=.\conf
set CLASSPATH=%CLASSPATH%;.\target\lib\*

set JAVA_OPTS= -server -XX:+UseNUMA -XX:+UseParallelGC -XX:+AggressiveOpts -Xms16M -Xmx256M

set JAVA_OPTS=%JAVA_OPTS% -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false -Dfile.encoding=UTF-8


@echo on

java %JAVA_OPTS% -cp "%CLASSPATH%" org.caudexorigo.jpt.sample.Main -r ".\wwwroot"
