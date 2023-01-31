package wstest.srv;

import java.util.Date;

import wstest.srv.actors.Address;
import wstest.srv.actors.COfLineItem;
import wstest.srv.actors.Customer;
import wstest.srv.actors.LineItem;
import wstest.srv.actors.Order;

public class OrderBL {
  public OrderBL() {}

  public Order getOrder(int orderId, int customerId, int numberLineItems) {

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
    customer.setCreditCardExpirationDate("12/12/2012");
    customer.setCreditCardNumber("892374389740");
    customer.setCustomerId(customerId);
    customer.setLastActivityDate(now);
    customer.setShippingAddress(ship);

    COfLineItem lines = new COfLineItem();
    for (int i = 0; i < numberLineItems; i++) {
      LineItem line = new LineItem();
      line.setItemId(i + 1);
      line.setOrderId(orderId);
      line.setOrderQuantity(1);
      line.setProductDescription("Test Product " + id);
      line.setProductId(i + 1);
      line.setUnitPrice((float) 5);
      lines.getLineItems().add(line);
    }

    Order order = new Order();
    order.setCustomer(customer);
    order.setLineItems(lines);
    order.setOrderDate(now);
    order.setOrderId(orderId);
    order.setOrderStatus(1);
    order.setOrderTotalAmount((float) 50);
    return order;
  }
}
