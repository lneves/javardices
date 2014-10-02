#!/bin/sh

cd $(dirname $0)

java \
-server -XX:+UseNUMA -XX:+UseParallelGC -XX:+AggressiveOpts \
-Xms16M -Xmx256M \
-Djava.awt.headless=true \
-Djava.net.preferIPv4Stack=true \
-Djava.net.preferIPv6Addresses=false \
-Dfile.encoding=UTF-8 \
-classpath "./conf:./target/lib/*" \
org.caudexorigo.jpt.sample.Main -r "./wwwroot"
