package db.tpce.driver;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import db.bench.DbRandom;
import db.bench.Profile;
import db.bench.TxMixGenerator;
import db.bench.tx.Tx;
import db.bench.tx.TxVoid;
import db.tpce.gen.CEInputGeneration;
import db.tpce.tx.TxBrokerVolume;
import db.tpce.tx.TxCustomerPosition;
import db.tpce.tx.TxMarketWatch;
import db.tpce.tx.TxSecurityDetail;
import db.tpce.tx.TxSimpleTradeLookup;
import db.tpce.tx.TxTradeStatus;

public class TpceMixGenerator implements TxMixGenerator
{
	private final DbRandom drand = new DbRandom();
	private final CEInputGeneration gen = new CEInputGeneration();

	private final String[] tx_arr;

	private final Map<String, Tx> tx_map = new ConcurrentHashMap<String, Tx>();
	private final Profile profile;

	public TpceMixGenerator(Profile profile, List<String> exclusion_list)
	{
		super();
		this.profile = profile;

		Tx broker_volume_tx = new TxBrokerVolume(gen);
		Tx customer_position_tx = new TxCustomerPosition(gen);
		Tx market_watch_tx = new TxMarketWatch(gen);
		Tx trade_status_tx = new TxTradeStatus(gen);
		Tx security_detail_tx = new TxSecurityDetail(gen);
		Tx trade_lookup_tx = new TxSimpleTradeLookup(gen);

		Tx trade_order_tx;
		Tx trade_result_tx;
		Tx market_feed_tx;
		Tx trade_update;

		if (profile == Profile.READ_ONLY)
		{
			trade_order_tx = new TxVoid("Trade-Order");
			trade_result_tx = new TxVoid("Trade-Result");
			market_feed_tx = new TxVoid("Market-Feed");
			trade_update = new TxVoid("Trade-Update");
		}
		else
		{
			// TODO: Implement this
			trade_order_tx = new TxVoid("Trade-Order");
			trade_result_tx = new TxVoid("Trade-Result");
			market_feed_tx = new TxVoid("Market-Feed");
			trade_update = new TxVoid("Trade-Update");
		}

		tx_map.put(broker_volume_tx.name(), broker_volume_tx);
		tx_map.put(customer_position_tx.name(), customer_position_tx);
		tx_map.put(market_watch_tx.name(), market_watch_tx);
		tx_map.put(trade_status_tx.name(), trade_status_tx);
		tx_map.put(trade_status_tx.name(), trade_status_tx);
		tx_map.put(trade_lookup_tx.name(), trade_lookup_tx);
		tx_map.put(security_detail_tx.name(), security_detail_tx);
		tx_map.put(trade_order_tx.name(), trade_order_tx);
		tx_map.put(trade_result_tx.name(), trade_result_tx);
		tx_map.put(market_feed_tx.name(), market_feed_tx);
		tx_map.put(trade_update.name(), trade_update);

		Set<String> k = tx_map.keySet();
		tx_arr = k.toArray(new String[k.size()]);

		for (String ex_tx : exclusion_list)
		{
			tx_map.put(ex_tx, new TxVoid(ex_tx));
		}
	}

	@Override
	public Profile profile()
	{
		return profile;
	}

	@Override
	public Tx nextTransaction()
	{
		double r = drand.rndDoubleRange(0.0, 100.0);

		if (r >= 0 && r < 4.9)
		{
			return tx_map.get("Broker-Volume");
		}
		else if (r >= 4.9 && r < 17.9)
		{
			return tx_map.get("Customer-Position");
		}
		else if (r >= 17.9 && r < 35.9)
		{
			return tx_map.get("Market-Watch");
		}
		else if (r >= 35.9 && r < 54.9)
		{
			return tx_map.get("Trade-Status");
		}
		else if (r >= 54.9 && r < 62.9)
		{
			return tx_map.get("Trade-Lookup");
		}
		else if (r >= 62.9 && r < 76.9)
		{
			return tx_map.get("Security-Detail");
		}
		else if (r >= 76.9 && r < 87)
		{
			return tx_map.get("Trade-Order");
		}
		else if (r >= 87 && r < 97)
		{
			return tx_map.get("Trade-Result");
		}
		else if (r >= 97 && r < 98)
		{
			return tx_map.get("Market-Feed");
		}
		else
		{
			return tx_map.get("Trade-Update");
		}
	}

	@Override
	public Tx txByName(String tx_name)
	{
		return tx_map.get(tx_name);
	}

	@Override
	public String[] transactionNames()
	{
		return tx_arr;
	}
}