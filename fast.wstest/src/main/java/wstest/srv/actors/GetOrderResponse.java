
package wstest.srv.actors;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:element xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="GetOrderResponse">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="ns:Order" name="GetOrderResult" minOccurs="1" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class GetOrderResponse {
  private Order getOrderResult;

  /**
   * Get the 'GetOrderResult' element value.
   * 
   * @return value
   */
  public Order getGetOrderResult() {
    return getOrderResult;
  }

  /**
   * Set the 'GetOrderResult' element value.
   * 
   * @param getOrderResult
   */
  public void setGetOrderResult(Order getOrderResult) {
    this.getOrderResult = getOrderResult;
  }
}
