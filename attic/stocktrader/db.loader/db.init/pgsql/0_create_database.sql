DROP DATABASE IF EXISTS stocktrader;

CREATE DATABASE stocktrader WITH OWNER = postgres ENCODING = 'UTF8' TABLESPACE = pg_default LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' CONNECTION LIMIT = -1;

CREATE USER trade WITH PASSWORD 'bengals#1';

ALTER DATABASE stocktrader OWNER TO trade;