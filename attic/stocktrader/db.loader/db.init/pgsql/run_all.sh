#!/bin/sh

cd $(dirname $0)

dropdb stocktrader
createdb stocktrader
psql -U postgres -h localhost -d stocktrader -f all_1_to_6.sql
