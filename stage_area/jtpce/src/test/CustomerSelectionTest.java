package test;

import db.tpce.gen.CustomerIdSelection;

public class CustomerSelectionTest
{
	public static void main(String[] args)
	{

		for (int i = 0; i < 500; i++)
		{
			System.out.println(CustomerIdSelection.getNonUniformRandom());
		}
	}
}