package test;

import org.caudexorigo.ErrorAnalyser;

import db.bench.output.TxOutput;
import db.tpce.gen.CEInputGeneration;
import db.tpce.tx.TxCustomerPosition;

public class ConsumerPositionTxest
{

	public static void main(String[] args)
	{
		try
		{
			final CEInputGeneration gen = new CEInputGeneration();
			TxCustomerPosition tx = new TxCustomerPosition(gen);

			for (int i = 0; i < 5000; i++)
			{
				TxOutput out = tx.execute();
				
				System.out.println(out);
				
				//Sleep.time(1000);
			}
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);

			r.printStackTrace();
		}

	}

}
