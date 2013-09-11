package db.bench;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeCounter implements Runnable
{
	private static final Logger log = LoggerFactory.getLogger(RuntimeCounter.class);
	private long t0 = 0L;
	private long t1 = 0L;

	private final double interval;
	private final List<ClientDriver> lst_clients;
	private final int target_count;

	public RuntimeCounter(double interval, List<ClientDriver> lst_clients, int iterations)
	{
		super();
		this.interval = interval;
		this.lst_clients = lst_clients;
		target_count = lst_clients.size() * iterations;
	}

	@Override
	public void run()
	{
		t1 = globalCount();
		double dt0 = (double) t0;
		double dt1 = (double) t1;
		double delta = (double) interval;
		double nr = (dt1 - dt0);

		log.info(String.format("Throughput: %.2f transactions/sec. %s transactions to finish %n", (nr / delta), (target_count - t1)));
		t0 = t1;
	}

	private long globalCount()
	{
		long global_total = 0L;
		for (ClientDriver client : lst_clients)
		{
			global_total += client.txCount();
		}
		return global_total;
	}
}
