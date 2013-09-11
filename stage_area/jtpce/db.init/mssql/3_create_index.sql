USE tpce;

SET ANSI_NULLS ON;
SET ANSI_NULL_DFLT_OFF ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET ARITHABORT ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET QUOTED_IDENTIFIER ON;
SET NUMERIC_ROUNDABORT OFF;

CREATE UNIQUE CLUSTERED INDEX pk_account_permission ON account_permission(ap_ca_id, ap_tax_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_address ON address(ad_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_broker ON broker(b_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_cash_transaction ON cash_transaction(ct_t_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_charge ON charge(ch_tt_id, ch_c_tier) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_commission_rate ON commission_rate(cr_c_tier, cr_tt_id, cr_ex_id, cr_from_qty) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_company_competitor ON company_competitor(cp_co_id, cp_comp_co_id, cp_in_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_company ON company(co_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_customer_account ON customer_account(ca_id) WITH (FILLFACTOR=80, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_customer ON customer(c_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_customer_taxrate ON customer_taxrate(cx_c_id, cx_tx_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_daily_market ON daily_market(dm_s_symb, dm_date) WITH (FILLFACTOR=100, MAXDOP=4, SORT_IN_TEMPDB=on)
GO

CREATE UNIQUE CLUSTERED INDEX pk_exchange ON exchange(ex_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_financial ON financial(fi_co_id, fi_year, fi_qtr) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_holding_history ON holding_history(hh_h_t_id, hh_t_id) WITH (FILLFACTOR=95, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_holding ON holding(h_t_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_holding_summary ON holding_summary(hs_ca_id, hs_s_symb) WITH (FILLFACTOR=95, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_industry ON industry(in_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_last_trade ON last_trade(lt_s_symb) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_news_item ON news_item(ni_id) WITH (FILLFACTOR=100, MAXDOP=4, SORT_IN_TEMPDB=on)
GO

CREATE UNIQUE CLUSTERED INDEX pk_news_xref ON news_xref(nx_co_id, nx_ni_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_sector ON sector(sc_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_security ON security(s_symb) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_settlement ON settlement(se_t_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_status_type ON status_type(st_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_taxrate ON taxrate(tx_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_trade_history ON trade_history(th_t_id, th_st_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_trade ON trade(t_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_trade_request ON trade_request(tr_t_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_trade_type ON trade_type(tt_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_watch_item ON watch_item(wi_wl_id, wi_s_symb) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_watch_list ON watch_list(wl_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE CLUSTERED INDEX pk_zip_code ON zip_code(zc_code) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX broker_nc1 ON broker(b_name, b_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX broker_nc2 ON broker(b_id, b_name) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX company_nc1 ON company(co_name, co_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX company_nc2 ON company(co_in_id, co_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX customer_account_nc1 ON customer_account(ca_c_id, ca_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX customer_account_nc2 ON customer_account(ca_b_id, ca_id, ca_c_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX customer_account_nc3 ON customer_account(ca_id, ca_b_id, ca_c_id, ca_tax_st, ca_name) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX customer_nc1 ON customer(c_tax_id, c_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX customer_nc2 ON customer(c_id, c_tier) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX daily_market_nc1 ON daily_market(dm_date, dm_s_symb) WITH (FILLFACTOR=100, MAXDOP=4, SORT_IN_TEMPDB=on)
GO

CREATE UNIQUE INDEX holding_history_nc1 ON holding_history(hh_t_id, hh_h_t_id) WITH (PAD_INDEX = on, FILLFACTOR = 100, MAXDOP=4, SORT_IN_TEMPDB = on)
GO

CREATE UNIQUE INDEX holding_nc1 ON holding(h_ca_id, h_s_symb, h_dts, h_t_id) WITH (PAD_INDEX = on, FILLFACTOR = 95, MAXDOP=4, SORT_IN_TEMPDB = on)
GO

CREATE UNIQUE INDEX industry_nc1 ON industry(in_name, in_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX industry_nc2 ON industry(in_sc_id, in_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX sector_nc1 ON sector(sc_name, sc_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX security_nc1 ON security(s_co_id, s_issue, s_ex_id, s_symb) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX security_nc2 ON security(s_co_id, s_num_out, s_symb) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX trade_nc1 ON trade(t_ca_id, t_dts, t_id) WITH (PAD_INDEX = on, FILLFACTOR = 95, MAXDOP=4, SORT_IN_TEMPDB = on)
GO

CREATE UNIQUE INDEX trade_nc2 ON trade(t_s_symb, t_dts, t_id) WITH (PAD_INDEX = on, FILLFACTOR = 95, MAXDOP=4, SORT_IN_TEMPDB = on)
GO

CREATE UNIQUE INDEX trade_request_nc1 ON trade_request(tr_b_id, tr_s_symb, tr_t_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX trade_request_nc2 ON trade_request(tr_s_symb, tr_t_id, tr_tt_id, tr_bid_price, tr_qty) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE UNIQUE INDEX tt_nc_index0 ON trade_type(tt_id, tt_is_mrkt, tt_is_sell, tt_name) WITH (FILLFACTOR=100)
GO

CREATE UNIQUE INDEX tt_nc_index1 ON trade_type(tt_id, tt_is_mrkt, tt_is_sell, tt_name) WITH (FILLFACTOR=100)
GO

CREATE UNIQUE INDEX tt_nc_index2 ON trade_type(tt_id, tt_is_mrkt, tt_is_sell, tt_name) WITH (FILLFACTOR=100)
GO

CREATE UNIQUE INDEX tt_nc_index3 ON trade_type(tt_id, tt_is_mrkt, tt_is_sell, tt_name) WITH (FILLFACTOR=100)
GO

CREATE UNIQUE INDEX tt_nc_index4 ON trade_type(tt_id, tt_is_mrkt, tt_is_sell, tt_name) WITH (FILLFACTOR=100)
GO

CREATE UNIQUE INDEX tt_nc_index5 ON trade_type(tt_id, tt_is_mrkt, tt_is_sell, tt_name) WITH (FILLFACTOR=100)
GO

CREATE UNIQUE INDEX tt_nc_index6 ON trade_type(tt_id, tt_is_mrkt, tt_is_sell, tt_name) WITH (FILLFACTOR=100)
GO

CREATE UNIQUE INDEX tt_nc_index7 ON trade_type(tt_id, tt_is_mrkt, tt_is_sell, tt_name) WITH (FILLFACTOR=100)
GO

CREATE UNIQUE INDEX watch_list_nc1 ON watch_list(wl_c_id, wl_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO

CREATE INDEX company_comp_nc1 ON company_competitor(cp_co_id) WITH (FILLFACTOR=100, MAXDOP=4)
GO
