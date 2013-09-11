package db.bench;

import java.util.List;

import org.caudexorigo.text.StringUtils;

public class Report extends Thread
{
	private final long start_ts;
	private final List<ClientDriver> lst_clients;
	private final String[] tx_names;

	public Report(long start_ts, List<ClientDriver> lst_clients, String[] tx_names)
	{
		super();
		this.start_ts = start_ts;
		this.lst_clients = lst_clients;
		this.tx_names = tx_names;
	}

	@Override
	public void run()
	{
		try
		{
			System.out.println();

			final long stop_ts = System.currentTimeMillis();
			final double elapsed = (double) (stop_ts - start_ts);

			long global_total = globalCount();

			double total_tx_rate = ((double) global_total) / (elapsed / 1000.0);

			System.out.println("");
			System.out.println("                                    ----------------   Response Time (ms)   ---------------                          ");
			System.out.println("Transaction          |    (%)Total |        Avg. |           s |         Min |         Max |     Total |  Rate(tx/s) ");
			System.out.println("---------------------+-------------+-------------+-------------+-------------+-------------+-----------+-------------");

			for (String tx_name : tx_names)
			{
				long total_tx = 0L;
				long total_fail_tx = 0L;
				double combined_mean_n = 0.0;
				double combined_mean = 0.0;
				double min = Double.MAX_VALUE;
				double max = Double.MIN_VALUE;

				for (ClientDriver client : lst_clients)
				{
					Collector c = client.getTxCollector(tx_name);

					if (c != null)
					{
						total_tx += c.getTxCount();
						total_fail_tx += c.getTxFailedCount();
						min = Math.min(min, c.getMin());
						max = Math.max(max, c.getMax());
						combined_mean_n += c.getMean() * ((double) c.getTxCount());
					}
				}

				if (total_tx == 0)
				{
					continue;
				}

				combined_mean = combined_mean_n / ((double) total_tx);

				double combined_var_n = 0.0;
				double combined_var = 0.0;
				double combined_s = 0.0;

				for (ClientDriver client : lst_clients)
				{
					Collector c = client.getTxCollector(tx_name);

					if (c != null)
					{
						combined_var_n += c.getTxCount() * (Math.pow(c.getStdDev(), 2) + Math.pow(combined_mean - c.getMean(), 2));
					}
				}

				combined_var = combined_var_n / ((double) total_tx);
				combined_s = Math.sqrt(combined_var);

				double pct_of_total = ((double) total_tx / (double) global_total) * 100.0;
				double tx_rate = ((double) total_tx) / (elapsed / 1000.0);
				String tname = StringUtils.rightPad(tx_name, 20);

				System.out.printf("%s |%s |%s |%s |%s |%s |%s |%s %n", tname, fnum(pct_of_total), fnum(combined_mean), fnum(combined_s), fnum(min), fnum(max), fnum(total_tx), fnum(tx_rate));
			}

			double time_taken = elapsed / 1000.0;
			System.out.println("\n--");
			System.out.printf("Clients: %s%n", lst_clients.size());
			System.out.printf("Time take for test: %.2f sec.%n", time_taken);
			System.out.printf("Total transactions: %s%n", global_total);
			System.out.printf("Avg. transaction rate: %.2f tx/s%n%n", total_tx_rate);
		}
		catch (Throwable te)
		{
			te.printStackTrace();
		}
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

	private final String fnum(long v)
	{
		String s = StringUtils.leftPad(Long.toString(v), 10);
		return s;
	}

	private final String fnum(double v)
	{
		String s = StringUtils.leftPad(String.format("%.2f", v), 12);
		return s;
	}
}
