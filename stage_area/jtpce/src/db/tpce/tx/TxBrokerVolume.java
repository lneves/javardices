package db.tpce.tx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.bench.Dml;
import db.bench.output.TxOutput;
import db.bench.tx.TxBase;
import db.tpce.common.BrokerVolume;
import db.tpce.gen.CEInputGeneration;
import db.tpce.input.TBrokerVolumeInput;
import db.tpce.output.TBrokerVolumeFrame1Output;

public class TxBrokerVolume extends TxBase
{
	private static final Logger log = LoggerFactory.getLogger(TxBrokerVolume.class);

	public static final int min_broker_list_len = 20;
	public static final int max_broker_list_len = 40;
	
	private static final String bv_f1_1_sql = Dml.load("broker_volume_frm1.sql");

	private static final ResultSetHandler<BrokerVolume> rs_ex = new ResultSetHandler<BrokerVolume>()
	{
		@Override
		public BrokerVolume process(ResultSet rs)
		{
			try
			{
				return new BrokerVolume(rs.getString("b_name"), rs.getLong("volume"));
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private final CEInputGeneration gen;
	private final String tx_name;

	public TxBrokerVolume(CEInputGeneration gen)
	{
		super();
		this.gen = gen;
		tx_name = "Broker-Volume";
	}

	@Override
	protected final TxOutput run()
	{
		int status = 0;

		final TBrokerVolumeInput frm1_input = gen.generateBrokerVolumeInput();

		if (log.isDebugEnabled())
		{
			log.debug(frm1_input.toString());
		}

		DbFetcher<BrokerVolume> db = new DbFetcher<BrokerVolume>();

		List<BrokerVolume> lst_broker_volume;

		Object[] sql_args = new String[max_broker_list_len + 1];			
		System.arraycopy(frm1_input.broker_list, 0, sql_args, 0, max_broker_list_len);
		sql_args[max_broker_list_len] = frm1_input.sector_name;
		
		lst_broker_volume = db.fetchList(bv_f1_1_sql, rs_ex, sql_args);

		if ((lst_broker_volume.size() < 0) || (lst_broker_volume.size() > max_broker_list_len))
		{
			status = -111;
		}
		TBrokerVolumeFrame1Output frm1_output = new TBrokerVolumeFrame1Output(status, lst_broker_volume);
		return frm1_output;
	}

	@Override
	public String name()
	{
		return tx_name;
	}
}