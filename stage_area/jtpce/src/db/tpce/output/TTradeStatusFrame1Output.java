package db.tpce.output;

import java.util.List;

import db.bench.output.TxOutput;
import db.tpce.common.TradeInfo;

public class TTradeStatusFrame1Output extends TxOutput
{
	public final List<TradeInfo> lst_trade_info;

	public TTradeStatusFrame1Output(int status, List<TradeInfo> lst_trade_info)
	{
		super(status);
		this.lst_trade_info = lst_trade_info;
	}

	@Override
	public String toString()
	{
		return String.format("TTradeStatusFrame1Output [%nlst_trade_info=%s%n, status=%s%n, tx_time=%s%n]", lst_trade_info, status, ((double) tx_time / 1000000.0));
	}
}