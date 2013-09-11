package trade.api;

import java.math.BigDecimal;
import java.util.Random;

public class TradeConfig
{
	private static Random rnd = new Random();
	private static final BigDecimal ONE = new BigDecimal("1.0");
	public static BigDecimal PENNY_STOCK_PRICE;
	public static BigDecimal PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER;

	static
	{
		PENNY_STOCK_PRICE = new BigDecimal(0.01);
		PENNY_STOCK_PRICE = PENNY_STOCK_PRICE.setScale(2, BigDecimal.ROUND_HALF_UP);
		PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER = new BigDecimal(600.0);
		PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal getRandomPriceChangeFactor()
	{
		// stocks are equally likely to go up or down
		double percentGain = rndFloat(1) + 0.5;
		// change factor is between +/- 50%
		BigDecimal percentGainBD = (new BigDecimal(percentGain)).setScale(2, BigDecimal.ROUND_HALF_UP);
		if (percentGainBD.doubleValue() <= 0.0)
		{
			percentGainBD = ONE;
		}

		return percentGainBD;
	}

	private static double random()
	{
		return rnd.nextDouble();
	}

	private static float rndFloat(int i)
	{
		return (new Float(random() * i)).floatValue();
	}
}