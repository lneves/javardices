#!/usr/bin/env bash

for url in $(cat page_urls.txt)
do

user_id=$(curl -s $url | egrep -m1 'link.*user_timeline.*rss' | sed -u 's/.*\/user_timeline//; s/.rss".*//')


if [ -n $user_id ]; then
	echo "$url;$user_id" | tee -a user_feed_info.txt
else
	echo "blank user name for $url"
fi

done
