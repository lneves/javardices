
USE tpce;


SET ANSI_NULLS ON;
SET ANSI_NULL_DFLT_OFF ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET ARITHABORT ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET QUOTED_IDENTIFIER ON;
SET NUMERIC_ROUNDABORT OFF;



DECLARE @Sql NVARCHAR(500) DECLARE @Cursor CURSOR
SET @Cursor = CURSOR FAST_FORWARD FOR

SELECT DISTINCT sql = 'ALTER TABLE [' + tc2.TABLE_NAME + '] DROP [' + rc1.CONSTRAINT_NAME + ']'
FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS rc1
LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc2 ON tc2.CONSTRAINT_NAME =rc1.CONSTRAINT_NAME

OPEN @Cursor FETCH NEXT FROM @Cursor INTO @Sql

WHILE (@@FETCH_STATUS = 0)

BEGIN

Exec SP_EXECUTESQL @Sql

FETCH NEXT FROM @Cursor INTO @Sql

END

CLOSE @Cursor DEALLOCATE @Cursor
GO


IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'account_permission')
DROP TABLE account_permission;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'address')
DROP TABLE address;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'broker')
DROP TABLE broker;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'company')
DROP TABLE company;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'company_competitor')
DROP TABLE company_competitor;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'customer')
DROP TABLE customer;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'customer_account')
DROP TABLE customer_account;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'customer_taxrate')
DROP TABLE customer_taxrate;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'daily_market')
DROP TABLE daily_market;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'financial')
DROP TABLE financial;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'last_trade')
DROP TABLE last_trade;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'news_item')
DROP TABLE news_item;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'news_xref')
DROP TABLE news_xref;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'security')
DROP TABLE security;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'watch_item')
DROP TABLE watch_item;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'watch_list')
DROP TABLE watch_list;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'cash_transaction')
DROP TABLE cash_transaction;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'holding')
DROP TABLE holding;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'holding_history')
DROP TABLE holding_history;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'holding_summary')
DROP TABLE holding_summary;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'settlement')
DROP TABLE settlement;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'trade')
DROP TABLE trade;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'trade_history')
DROP TABLE trade_history;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'trade_request')
DROP TABLE trade_request;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'account_permission')
DROP TABLE account_permission;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'address')
DROP TABLE address;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'broker')
DROP TABLE broker;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'company')
DROP TABLE company;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'company_competitor')
DROP TABLE company_competitor;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'customer')
DROP TABLE customer;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'customer_account')
DROP TABLE customer_account;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'customer_taxrate')
DROP TABLE customer_taxrate;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'daily_market')
DROP TABLE daily_market;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'financial')
DROP TABLE financial;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'last_trade')
DROP TABLE last_trade;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'news_item')
DROP TABLE news_item;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'news_xref')
DROP TABLE news_xref;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'security')
DROP TABLE security;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'watch_item')
DROP TABLE watch_item;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'watch_list')
DROP TABLE watch_list;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'account_permission')
DROP TABLE account_permission;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'address')
DROP TABLE address;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'broker')
DROP TABLE broker;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'company')
DROP TABLE company;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'company_competitor')
DROP TABLE company_competitor;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'customer')
DROP TABLE customer;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'customer_account')
DROP TABLE customer_account;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'customer_taxrate')
DROP TABLE customer_taxrate;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'daily_market')
DROP TABLE daily_market;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'financial')
DROP TABLE financial;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'last_trade')
DROP TABLE last_trade;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'news_item')
DROP TABLE news_item;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'news_xref')
DROP TABLE news_xref;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'security')
DROP TABLE security;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'watch_item')
DROP TABLE watch_item;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'watch_list')
DROP TABLE watch_list;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'charge')
DROP TABLE charge;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'commission_rate')
DROP TABLE commission_rate;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'exchange')
DROP TABLE exchange;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'zip_code')
DROP TABLE zip_code;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'trade_type')
DROP TABLE trade_type;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'tid_ranges')
DROP TABLE tid_ranges;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'taxrate')
DROP TABLE taxrate;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'status_type')
DROP TABLE status_type;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'sector')
DROP TABLE sector;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'industry')
DROP TABLE industry;



CREATE TABLE account_permission (
  ap_ca_id bigint NOT NULL,
  ap_acl char(4) NOT NULL,
  ap_tax_id varchar(20) NOT NULL,
  ap_l_name varchar(25) NOT NULL,
  ap_f_name varchar(20) NOT NULL
);


CREATE TABLE customer (
  c_id bigint NOT NULL,
  c_tax_id varchar(20) NOT NULL,
  c_st_id char(4) NOT NULL,
  c_l_name varchar(25) NOT NULL,
  c_f_name varchar(20) NOT NULL,
  c_m_name char(1),
  c_gndr char(1),
  c_tier smallint NOT NULL,
  c_dob date NOT NULL,
  c_ad_id bigint NOT NULL,
  c_ctry_1 varchar(3),
  c_area_1 varchar(3),
  c_local_1 varchar(10),
  c_ext_1 varchar(5),
  c_ctry_2 varchar(3),
  c_area_2 varchar(3),
  c_local_2 varchar(10),
  c_ext_2 varchar(5),
  c_ctry_3 varchar(3),
  c_area_3 varchar(3),
  c_local_3 varchar(10),
  c_ext_3 varchar(5),
  c_email_1 varchar(50),
  c_email_2 varchar(50)
);


CREATE TABLE customer_account (
  ca_id bigint NOT NULL,
  ca_b_id bigint NOT NULL,
  ca_c_id bigint NOT NULL,
  ca_name varchar(50),
  ca_tax_st smallint NOT NULL,
  ca_bal decimal(12,2) NOT NULL
);


CREATE TABLE customer_taxrate (
  cx_tx_id char(4) NOT NULL,
  cx_c_id bigint NOT NULL
);


CREATE TABLE holding (
  h_t_id bigint NOT NULL,
  h_ca_id bigint NOT NULL,
  h_s_symb char(15) NOT NULL,
  h_dts datetime NOT NULL,
  h_price decimal(8,2) NOT NULL CHECK (h_price > 0),
  h_qty integer NOT NULL
);


CREATE TABLE holding_history (
  hh_h_t_id bigint NOT NULL,
  hh_t_id bigint NOT NULL,
  hh_before_qty integer NOT NULL,
  hh_after_qty integer NOT NULL
);


CREATE TABLE holding_summary (
  hs_ca_id bigint NOT NULL,
  hs_s_symb char(15) NOT NULL,
  hs_qty integer NOT NULL
);


CREATE TABLE watch_item (
  wi_wl_id bigint NOT NULL,
  wi_s_symb char(15) NOT NULL
);


CREATE TABLE watch_list (
  wl_id bigint NOT NULL,
  wl_c_id bigint NOT NULL
);


CREATE TABLE broker (
  b_id bigint NOT NULL,
  b_st_id char(4) NOT NULL,
  b_name varchar(49) NOT NULL,
  b_num_trades integer NOT NULL,
  b_comm_total decimal(12,2) NOT NULL
);


CREATE TABLE cash_transaction (
  ct_t_id bigint NOT NULL,
  ct_dts datetime NOT NULL,
  ct_amt decimal(10,2) NOT NULL,
  ct_name varchar(100)
);


CREATE TABLE charge (
  ch_tt_id char(3) NOT NULL,
  ch_c_tier smallint NOT NULL,
  ch_chrg decimal(10,2) NOT NULL CHECK (ch_chrg > 0)
);


CREATE TABLE commission_rate (
  cr_c_tier smallint NOT NULL,
  cr_tt_id char(3) NOT NULL,
  cr_ex_id char(6) NOT NULL,
  cr_from_qty integer NOT NULL CHECK (cr_from_qty >= 0),
  cr_to_qty integer NOT NULL,
  cr_rate decimal(5,2) NOT NULL CHECK (cr_rate >= 0)
);


CREATE TABLE settlement (
  se_t_id bigint NOT NULL,
  se_cash_type varchar(40) NOT NULL,
  se_cash_due_date date NOT NULL,
  se_amt decimal(10,2) NOT NULL
);


CREATE TABLE trade (
  t_id bigint NOT NULL,
  t_dts datetime NOT NULL,
  t_st_id char(4) NOT NULL,
  t_tt_id char(3) NOT NULL,
  t_is_cash bit NOT NULL,
  t_s_symb char(15) NOT NULL,
  t_qty integer NOT NULL CHECK (t_qty > 0),
  t_bid_price decimal(8,2) NOT NULL CHECK (t_bid_price > 0),
  t_ca_id bigint NOT NULL,
  t_exec_name varchar(49) NOT NULL,
  t_trade_price decimal(8,2),
  t_chrg decimal(10,2) NOT NULL CHECK (t_chrg >= 0),
  t_comm decimal(10,2) NOT NULL CHECK (t_comm >= 0),
  t_tax decimal(10,2) NOT NULL CHECK (t_tax >= 0),
  t_lifo bit NOT NULL
);


CREATE TABLE trade_history (
  th_t_id bigint NOT NULL,
  th_dts datetime NOT NULL,
  th_st_id char(4) NOT NULL
);


CREATE TABLE trade_request (
  tr_t_id bigint NOT NULL,
  tr_tt_id char(3) NOT NULL,
  tr_s_symb char(15) NOT NULL,
  tr_qty integer NOT NULL CHECK (tr_qty > 0),
  tr_bid_price decimal(8,2) NOT NULL CHECK (tr_bid_price > 0),
  tr_b_id bigint NOT NULL
);


CREATE TABLE trade_type (
  tt_id char(3) NOT NULL,
  tt_name varchar(12) NOT NULL,
  tt_is_sell bit NOT NULL,
  tt_is_mrkt bit NOT NULL
);


CREATE TABLE company (
  co_id bigint NOT NULL,
  co_st_id char(4) NOT NULL,
  co_name varchar(60) NOT NULL,
  co_in_id char(2) NOT NULL,
  co_sp_rate char(4) NOT NULL,
  co_ceo varchar(46) NOT NULL,
  co_ad_id bigint NOT NULL,
  co_desc varchar(150) NOT NULL,
  co_open_date date NOT NULL
);


CREATE TABLE company_competitor (
  cp_co_id bigint NOT NULL,
  cp_comp_co_id bigint NOT NULL,
  cp_in_id char(2) NOT NULL
);


CREATE TABLE daily_market (
  dm_date date NOT NULL,
  dm_s_symb char(15) NOT NULL,
  dm_close decimal(8,2) NOT NULL,
  dm_high decimal(8,2) NOT NULL,
  dm_low decimal(8,2) NOT NULL,
  dm_vol bigint NOT NULL
);


CREATE TABLE exchange (
  ex_id char(6) NOT NULL,
  ex_name varchar(100) NOT NULL,
  ex_num_symb integer NOT NULL,
  ex_open smallint NOT NULL,
  ex_close smallint NOT NULL,
  ex_desc varchar(150),
  ex_ad_id bigint NOT NULL
);


CREATE TABLE financial (
  fi_co_id bigint NOT NULL,
  fi_year smallint NOT NULL,
  fi_qtr smallint NOT NULL,
  fi_qtr_start_date date NOT NULL,
  fi_revenue decimal(15,2) NOT NULL,
  fi_net_earn decimal(15,2) NOT NULL,
  fi_basic_eps decimal(10,2) NOT NULL,
  fi_dilut_eps decimal(10,2) NOT NULL,
  fi_margin decimal(10,2) NOT NULL,
  fi_inventory decimal(15,2) NOT NULL,
  fi_assets decimal(15,2) NOT NULL,
  fi_liability decimal(15,2) NOT NULL,
  fi_out_basic bigint NOT NULL,
  fi_out_dilut bigint NOT NULL
);


CREATE TABLE industry (
  in_id char(2) NOT NULL,
  in_name varchar(50) NOT NULL,
  in_sc_id char(2) NOT NULL
);


CREATE TABLE last_trade (
  lt_s_symb char(15) NOT NULL,
  lt_dts datetime NOT NULL,
  lt_price decimal(8,2) NOT NULL,
  lt_open_price decimal(8,2) NOT NULL,
  lt_vol bigint NOT NULL
);


CREATE TABLE news_item (
  ni_id bigint NOT NULL,
  ni_headline varchar(80) NOT NULL,
  ni_summary varchar(255) NOT NULL,
  ni_item varchar(max) NOT NULL,
  ni_dts datetime NOT NULL,
  ni_source varchar(30) NOT NULL,
  ni_author varchar(30)
);


CREATE TABLE news_xref (
  nx_ni_id bigint NOT NULL,
  nx_co_id bigint NOT NULL
);


CREATE TABLE sector (
  sc_id char(2) NOT NULL,
  sc_name varchar(30) NOT NULL
);


CREATE TABLE security (
  s_symb char(15) NOT NULL,
  s_issue char(6) NOT NULL,
  s_st_id char(4) NOT NULL,
  s_name varchar(70) NOT NULL,
  s_ex_id char(6) NOT NULL,
  s_co_id bigint NOT NULL,
  s_num_out bigint NOT NULL,
  s_start_date date NOT NULL,
  s_exch_date date NOT NULL,
  s_pe decimal(10,2) NOT NULL,
  s_52wk_high decimal(8,2) NOT NULL,
  s_52wk_high_date date NOT NULL,
  s_52wk_low decimal(8,2) NOT NULL,
  s_52wk_low_date date NOT NULL,
  s_dividend decimal(10,2) NOT NULL,
  s_yield decimal(5,2) NOT NULL
);


CREATE TABLE address (
  ad_id bigint NOT NULL,
  ad_line1 varchar(80),
  ad_line2 varchar(80),
  ad_zc_code char(12) NOT NULL,
  ad_ctry varchar(80)
);


CREATE TABLE status_type (
  st_id char(4) NOT NULL,
  st_name char(10) NOT NULL
);


CREATE TABLE taxrate (
  tx_id char(4) NOT NULL,
  tx_name varchar(50) NOT NULL,
  tx_rate decimal(6,5) NOT NULL CHECK (tx_rate >= 0)
);


CREATE TABLE zip_code (
  zc_code char(12) NOT NULL,
  zc_town varchar(80) NOT NULL,
  zc_div varchar(80) NOT NULL
);

