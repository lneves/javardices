#!/bin/sh

cd $(dirname $0)

java \
-server -XX:+UseNUMA -XX:+UseParallelGC -XX:+AggressiveOpts \
-Xms16M -Xmx256M \
-Djava.awt.headless=true \
-Djava.net.preferIPv4Stack=true \
-Djava.net.preferIPv6Addresses=false \
-Dfile.encoding=UTF-8 \
-jar "./target/fast-wstest-1.0-SNAPSHOT-jar-with-dependencies.jar"
