package db.tpce.output;

import java.util.List;

import db.bench.output.TxOutput;
import db.tpce.common.TradeLookup1;
import db.tpce.common.TradeLookup2;
import db.tpce.common.TradeLookup3;
import db.tpce.common.TradeLookup4;
import db.tpce.common.TradeLookupHistory;

public class TTradeLookupOutput extends TxOutput
{
	public final List<TradeLookup1> lst_trades_frm1;
	public final List<TradeLookup2> lst_trades_frm2;
	public final List<TradeLookup3> lst_trades_frm3;
	public final List<TradeLookup4> lst_trades_frm4;
	public final List<TradeLookupHistory> lst_trades_history;
	public final int frame_executed;

	public TTradeLookupOutput(int status, int frameExecuted, List<TradeLookup1> lstTradesFrm1, List<TradeLookup2> lstTradesFrm2, List<TradeLookup3> lstTradesFrm3, List<TradeLookup4> lstTradesFrm4, List<TradeLookupHistory> lstTradesHistory)
	{
		super(status);
		frame_executed = frameExecuted;
		lst_trades_frm1 = lstTradesFrm1;
		lst_trades_frm2 = lstTradesFrm2;
		lst_trades_frm3 = lstTradesFrm3;
		lst_trades_frm4 = lstTradesFrm4;
		lst_trades_history = lstTradesHistory;
	}

	@Override
	public String toString()
	{
		return String.format("TTradeLookupOutput [%nframe_executed=%s%n, lst_trades_frm1=%s%n, lst_trades_frm2=%s%n, lst_trades_frm3=%s%n, lst_trades_frm4=%s%n, lst_trades_history=%s%n, status=%s%n, tx_time=%s%n]", frame_executed, lst_trades_frm1, lst_trades_frm2, lst_trades_frm3, lst_trades_frm4, lst_trades_history, status, ((double) tx_time / 1000000.0));
	}
}