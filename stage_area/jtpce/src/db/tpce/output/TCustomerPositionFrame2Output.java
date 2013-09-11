package db.tpce.output;

import java.util.List;

import db.bench.output.TxOutput;
import db.tpce.common.TradeHistory;

public class TCustomerPositionFrame2Output extends TxOutput
{
	public final List<TradeHistory> lst_trade_history;
	public final double hist_len;

	public TCustomerPositionFrame2Output(int status, List<TradeHistory> lst_trade_history)
	{
		super(status);
		this.lst_trade_history = lst_trade_history;
		hist_len = lst_trade_history.size();
	}

	@Override
	public String toString()
	{
		return String.format("TCustomerPositionFrame2Output [%nlst_trade_history=%s%n, hist_len=%s, status=%s%n, tx_time=%s%n]", lst_trade_history, hist_len, status, ((double) tx_time / 1000000.0));
	}
}