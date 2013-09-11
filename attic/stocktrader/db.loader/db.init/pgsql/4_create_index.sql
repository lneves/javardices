CREATE INDEX account_profile_userid_idx ON account (profile_userid);
CREATE INDEX holding_account_accountid_holdingid_quote_symbol_idx ON holding (account_accountid, holdingid, quote_symbol);
CREATE INDEX holding_account_accountid_idx ON holding (account_accountid);
CREATE INDEX orders_account_accountid_idx ON orders (account_accountid);
CREATE INDEX orders_account_accountid_orderid_idx ON orders (account_accountid, orderid);
CREATE INDEX orders_orderstatus_account_accountid_idx ON orders (orderstatus, account_accountid);
CREATE INDEX quote_open1_idx ON quote (open1);
CREATE INDEX quote_price_idx ON quote (price);
