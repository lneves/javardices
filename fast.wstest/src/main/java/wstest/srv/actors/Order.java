
package wstest.srv.actors;

import java.util.Date;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:complexType xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Order">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="OrderId" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:int" name="OrderStatus" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:dateTime" name="OrderDate" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:float" name="OrderTotalAmount" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="ns:Customer" name="Customer" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="ns:COfLineItem" name="LineItems" minOccurs="1" maxOccurs="1"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Order {
  private int orderId;
  private int orderStatus;
  private Date orderDate;
  private Float orderTotalAmount;
  private Customer customer;
  private COfLineItem lineItems;

  /**
   * Get the 'OrderId' element value.
   * 
   * @return value
   */
  public int getOrderId() {
    return orderId;
  }

  /**
   * Set the 'OrderId' element value.
   * 
   * @param orderId
   */
  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  /**
   * Get the 'OrderStatus' element value.
   * 
   * @return value
   */
  public int getOrderStatus() {
    return orderStatus;
  }

  /**
   * Set the 'OrderStatus' element value.
   * 
   * @param orderStatus
   */
  public void setOrderStatus(int orderStatus) {
    this.orderStatus = orderStatus;
  }

  /**
   * Get the 'OrderDate' element value.
   * 
   * @return value
   */
  public Date getOrderDate() {
    return orderDate;
  }

  /**
   * Set the 'OrderDate' element value.
   * 
   * @param orderDate
   */
  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  /**
   * Get the 'OrderTotalAmount' element value.
   * 
   * @return value
   */
  public Float getOrderTotalAmount() {
    return orderTotalAmount;
  }

  /**
   * Set the 'OrderTotalAmount' element value.
   * 
   * @param orderTotalAmount
   */
  public void setOrderTotalAmount(Float orderTotalAmount) {
    this.orderTotalAmount = orderTotalAmount;
  }

  /**
   * Get the 'Customer' element value.
   * 
   * @return value
   */
  public Customer getCustomer() {
    return customer;
  }

  /**
   * Set the 'Customer' element value.
   * 
   * @param customer
   */
  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  /**
   * Get the 'LineItems' element value.
   * 
   * @return value
   */
  public COfLineItem getLineItems() {
    return lineItems;
  }

  /**
   * Set the 'LineItems' element value.
   * 
   * @param lineItems
   */
  public void setLineItems(COfLineItem lineItems) {
    this.lineItems = lineItems;
  }
}
