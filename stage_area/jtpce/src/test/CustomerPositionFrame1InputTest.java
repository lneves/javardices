package test;

import db.tpce.gen.CEInputGeneration;
import db.tpce.input.TCustomerPositionFrame1Input;

public class CustomerPositionFrame1InputTest
{
	public static void main(String[] args)
	{
		final CEInputGeneration gen = new CEInputGeneration();
		
		for (int i = 0; i < 50; i++)
		{
			TCustomerPositionFrame1Input cpi_i = gen.generateCustomerPositionInput();
			System.out.println(cpi_i);
		}
	}
}