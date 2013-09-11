package db.bench;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.bench.output.TxError;
import db.bench.output.TxOutput;
import db.bench.tx.Tx;
import db.bench.tx.TxVoid;

public class ClientDriver implements Runnable
{
	private static final Logger log = LoggerFactory.getLogger(ClientDriver.class);

	private final Map<String, Collector> tx_collectors = new HashMap<String, Collector>();

	private final CountDownLatch latch;

	private final int iterations;
	private final String client_name;

	private final TxMixGenerator mix_gen;
	private long counter = 0L;

	public ClientDriver(String client_name, CountDownLatch latch, int iterations, TxMixGenerator mix_gen)
	{
		super();
		this.client_name = client_name;
		this.latch = latch;
		this.iterations = iterations;
		this.mix_gen = mix_gen;

		String[] tx_names = mix_gen.transactionNames();

		for (String tx_name : tx_names)
		{
			tx_collectors.put(tx_name, new Collector());
		}
	}

	@Override
	public void run()
	{
		log.info("'{}' started. Will execute {} iterations", client_name, iterations);

		int i = iterations;
		while (i-- > 0)
		{

			Tx tx = mix_gen.nextTransaction();

			if (tx instanceof TxVoid)
			{
				i++;
			}
			else
			{
				runTx(tx);
			}
		}

		latch.countDown();
	}

	private void runTx(Tx tx)
	{
		TxOutput tx_out = tx.execute();

		if (tx_out instanceof TxError)
		{
			log.error(tx_out.toString());
		}

		Collector c = tx_collectors.get(tx.name());

		c.addResult(tx_out.status, tx_out.tx_time);
		counter++;

		if (log.isDebugEnabled())
		{
			log.debug(tx_out.toString());
		}
	}

	public Collector getTxCollector(String tx_name)
	{
		return tx_collectors.get(tx_name);
	}

	public long txCount()
	{
		return counter;
	}
}