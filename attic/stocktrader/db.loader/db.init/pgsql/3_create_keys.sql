ALTER TABLE ONLY account ADD CONSTRAINT pk_account PRIMARY KEY (accountid);
ALTER TABLE ONLY accountprofile ADD CONSTRAINT pk_accountprofile PRIMARY KEY (userid);
ALTER TABLE ONLY holding ADD CONSTRAINT pk_holding PRIMARY KEY (holdingid);
ALTER TABLE ONLY orders ADD CONSTRAINT pk_order PRIMARY KEY (orderid);
ALTER TABLE ONLY quote ADD CONSTRAINT pk_quote PRIMARY KEY (symbol);

-- CLUSTER account USING pk_account;
-- CLUSTER accountprofile USING pk_accountprofile;
-- CLUSTER holding USING pk_holding;
-- CLUSTER orders USING pk_order;
-- CLUSTER quote USING pk_quote;
