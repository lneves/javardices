package db.bench;

import java.util.Random;

import db.tpce.common.MiscConstants;

public class DbRandom
{
	private final Random _rand = new Random();

	private static final long C_RUN = 128;

	/**
	 * Returns a pseudorandom, uniformly distributed {@code int} value between 0 (inclusive) and the specified value (inclusive), drawn from this random number generator's
	 * sequence.
	 */
	public int rndInt(int max)
	{
		return _rand.nextInt(max + 1);
	}

	public int rndIntRange(int min, int max)
	{
		return (int) (_rand.nextDouble() * (max - min + 1) + min);
	}

	public long rndLongRange(long min, long max)
	{
		return (long) (_rand.nextDouble() * (max - min + 1) + min);
	}

	public double rndDoubleRange(double min, double max)
	{
		return (_rand.nextDouble() * (max - min + 1) + min);
	}

	public double rndDoubleIncrRange(double min, double max, double incr)
	{
		long width = (long) ((max - min) / incr); // need [0..width], so no +1
		return min + ((double) rndLongRange(0, width) * incr);
	}

	// returns TRUE or FALSE, with the chance of TRUE being as specified by (percent)
	public final boolean rndPercent(int percent)
	{
		return (rndIntRange(1, 100) <= percent);
	}

	public String rndAlphaNumFormatted(String format)
	{
		char[] c_format = format.toCharArray();
		char[] dest = new char[c_format.length];

		for (int i = 0; i < c_format.length; i++)
		{
			char c = c_format[i];

			if (c == 'a')
			{
				dest[i] = MiscConstants.UpperCaseLetters[rndIntRange(0, 25)];
			}
			else if (c == 'n')
			{
				dest[i] = MiscConstants.Numerals[rndIntRange(0, 9)];
			}
		}

		return new String(dest);
	}

	public long nonUniformRandom(long min, long max, int A, int s)
	{
		return (((rndLongRange(min, max) | (rndLongRange(0, A) << s)) % (max - min + 1)) + min);
	}

	public long nonUniformRandom(long min, long max)
	{
		long A;

		if (min >= 0 && max <= 999)
		{
			A = 255;
		}
		else if (min >= 1 && max <= 300)
		{
			A = 1023;
		}
		else if (min >= 1 && max <= 100000)
		{
			A = 8191;
		}
		else
		{
			A = 131056;
		}

		return (((rndLongRange(0, A) | rndLongRange(min, max)) + C_RUN) % (max - min + 1)) + min;
	}
}