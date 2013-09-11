package test;

import org.caudexorigo.concurrent.Sleep;

import db.tpce.gen.CEInputGeneration;

public class BrokerVolumeFrame1InputTest
{
	public static void main(String[] args)
	{
		final CEInputGeneration gen = new CEInputGeneration();
		for (int i = 0; i < 50; i++)
		{
			System.out.println(gen.generateBrokerVolumeInput());
			
			Sleep.time(1000);
		}
	}
}