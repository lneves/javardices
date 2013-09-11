package test;

import org.caudexorigo.ErrorAnalyser;

import db.tpce.gen.BrokerSelection;

public class RandomBrokerTest
{
	public static void main(String[] args)
	{
		try
		{
			for (int i = 0; i < 5000; i++)
			{
				System.out.println(BrokerSelection.getRandom());
			}
		}
		catch (Throwable t)
		{
			Throwable rootCause = ErrorAnalyser.findRootCause(t);

			rootCause.printStackTrace();
		}
	}
}
