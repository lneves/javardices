
package wstest.srv.actors;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:element xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="GetOrder">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="xs:int" name="orderId" minOccurs="1" maxOccurs="1"/>
 *       &lt;xs:element type="xs:int" name="customerId" minOccurs="1" maxOccurs="1"/>
 *       &lt;xs:element type="xs:int" name="messageSize" minOccurs="1" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class GetOrder
{
	private int orderId;
	private int customerId;
	private int messageSize;

	/**
	 * Get the 'orderId' element value.
	 * 
	 * @return value
	 */
	public int getOrderId()
	{
		return orderId;
	}

	/**
	 * Set the 'orderId' element value.
	 * 
	 * @param orderId
	 */
	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}

	/**
	 * Get the 'customerId' element value.
	 * 
	 * @return value
	 */
	public int getCustomerId()
	{
		return customerId;
	}

	/**
	 * Set the 'customerId' element value.
	 * 
	 * @param customerId
	 */
	public void setCustomerId(int customerId)
	{
		this.customerId = customerId;
	}

	/**
	 * Get the 'messageSize' element value.
	 * 
	 * @return value
	 */
	public int getMessageSize()
	{
		return messageSize;
	}

	/**
	 * Set the 'messageSize' element value.
	 * 
	 * @param messageSize
	 */
	public void setMessageSize(int messageSize)
	{
		this.messageSize = messageSize;
	}
}
