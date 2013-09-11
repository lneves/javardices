package db.bench;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

public class Collector
{
	private long counter = 0L;
	private long f_counter = 0L;
	private final SummaryStatistics stats = new SummaryStatistics();

	public Collector()
	{
		super();
	}

	public void addResult(int status, long rp)
	{
		counter++;

		if (status < 0)
		{
			f_counter++;
		}

		stats.addValue((double) rp);
	}

	public long getTxCount()
	{
		return counter;
	}

	public long getTxFailedCount()
	{
		return f_counter;
	}

	public double getMin()
	{
		return stats.getMin() / 1000000.0;
	}

	public double getMax()
	{
		return stats.getMax() / 1000000.0;
	}

	public double getMean()
	{
		return stats.getMean() / 1000000.0;
	}

	public double getStdDev()
	{
		return stats.getStandardDeviation() / 1000000.0;
	}

	public double getVariance()
	{
		return stats.getVariance() / 1000000.0;
	}
}