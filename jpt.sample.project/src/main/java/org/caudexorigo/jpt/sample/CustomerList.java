package org.caudexorigo.jpt.sample;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.jpt.web.HttpJptController;

public class CustomerList extends HttpJptController
{
	private static final int DEFAULT_COUNT = 20;
	private static final int MAX_COUNT = 200;

	private List<Customer> customers;

	public List<Customer> getCustomers()
	{
		return customers;
	}

	@Override
	public void init()
	{
		String s_count = this.getHttpContext().getParameter("count");
		int p_count = DEFAULT_COUNT;

		if (StringUtils.isNotBlank(s_count))
		{
			try
			{
				p_count = Integer.parseInt(s_count);
			}
			catch (Throwable t)
			{
				// ignore
			}
		}

		int count = Math.min(MAX_COUNT, p_count);

		customers = CustomerService.fetch(count);
	}
}