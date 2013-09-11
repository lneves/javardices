package test;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.concurrent.Sleep;

import db.bench.output.TxOutput;
import db.tpce.gen.CEInputGeneration;
import db.tpce.tx.TxBrokerVolume;

public class BrokerVolumeTxTest
{

	public static void main(String[] args)
	{
		try
		{
			final CEInputGeneration gen = new CEInputGeneration();
			TxBrokerVolume tx = new TxBrokerVolume(gen);

			for (int i = 0; i < 5000; i++)
			{
				TxOutput out = tx.execute();
				
				System.out.println(out);
				
				Sleep.time(1000);
			}
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);

			r.printStackTrace();
		}

	}

}
