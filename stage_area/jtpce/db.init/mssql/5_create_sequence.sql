USE tpce;


SET ANSI_NULLS ON;
SET ANSI_NULL_DFLT_OFF ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET ARITHABORT ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET QUOTED_IDENTIFIER ON;
SET NUMERIC_ROUNDABORT OFF;

IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'tid_ranges')
DROP TABLE tid_ranges;

CREATE TABLE tid_ranges (
  spid smallint primary key,
  next_t_id bigint,
  max_t_id bigint
);


 -- initialize the tid range table
-- define a range large enough for the longest possible run
-- the select statement gets the maximum initial/run time t_id value

INSERT INTO tid_ranges 
SELECT -1, max(t_id) + 1, 100000 FROM trade;



IF EXISTS ( SELECT name FROM sysobjects WHERE name = 'get_next_trade_id' )
DROP PROCEDURE get_next_trade_id;
GO

CREATE PROCEDURE get_next_trade_id
 @next_t_id bigint OUTPUT
AS
BEGIN
 DECLARE @max_t_id bigint

 SET @next_t_id = NULL

 -- get the next value for this spid
 UPDATE tid_ranges
 SET
  @next_t_id = next_t_id,
  next_t_id = next_t_id + 1
 WHERE
  spid = @@spid
 AND next_t_id <= max_t_id

 -- in the normal case, we are done
 IF @next_t_id IS NOT NULL RETURN

 -- getting this far indicates that either there is not an entry in tid_ranges for this spid or
 -- the end of the range was reached.
 -- both the above cases are handled below
 UPDATE tid_ranges
 SET
  @next_t_id = next_t_id
  , @max_t_id = next_t_id + max_t_id - 1
  , next_t_id = next_t_id + max_t_id
 WHERE spid = -1

 -- if it exists, delete the entry for the current spid
 DELETE FROM tid_ranges WHERE spid = @@spid

 -- insert a new entry for the current spid
 INSERT INTO tid_ranges VALUES (@@spid, @next_t_id + 1, @max_t_id)

END
;
