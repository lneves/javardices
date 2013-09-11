package org.caudexorigo.wstest.ws.api;

import java.util.Date;

import org.caudexorigo.wstest.domain.Address;
import org.caudexorigo.wstest.domain.COfLineItem;
import org.caudexorigo.wstest.domain.Customer;
import org.caudexorigo.wstest.domain.LineItem;
import org.caudexorigo.wstest.domain.Order;

public class OrderBL
{
	public Order getOrder(int orderId, int customerId, int numberLineItems)
	{
		int id = customerId;
		Date now = new Date();

		Address ship = new Address();
		ship.setAddress1("Ship StreetAddress " + id);
		ship.setAddress2("Street Address Line 2 " + id);
		ship.setCity("City " + id);
		ship.setFirstName("Ship FirstName " + id);
		ship.setLastName("Ship LastName " + id);
		ship.setState("State " + id);
		ship.setZip("12345");
		Address bill = new Address();
		bill.setAddress1("Bill StreetAddress " + id);
		bill.setAddress2("Street Address Line 2 " + id);
		bill.setCity("City " + id);
		bill.setFirstName("Bill FirstName " + id);
		bill.setLastName("Bil1 LastName " + id);
		bill.setState("State " + id);
		bill.setZip("12345");

		Customer customer = new Customer();
		customer.setBillingAddress(bill);
		customer.setContactFirstName("FirstName " + id);
		customer.setContactLastName("LastName " + id);
		customer.setContactPhone("425-882-8080");
		customer.setCreditCardExpirationDate("12/12/2030");
		customer.setCreditCardNumber("892374389740");
		customer.setCustomerId(customerId);
		customer.setLastActivityDate(now);
		customer.setShippingAddress(ship);

		COfLineItem lines = new COfLineItem();
		for (int i = 0; i < numberLineItems; i++)
		{
			LineItem line = new LineItem();
			line.setItemId(i + 1);
			line.setOrderId(orderId);
			line.setOrderQuantity(1);
			line.setProductDescription("Test Product " + id);
			line.setProductId(i + 1);
			line.setUnitPrice((float) 5);
			lines.addLineItem(line);
		}

		Order order = new Order();
		order.setCustomer(customer);
		order.setCOfLineItem(lines);
		order.setOrderDate(now);
		order.setOrderId(orderId);
		order.setOrderStatus(1);
		order.setOrderTotalAmount((float) 50);
		return order;
	}	
}