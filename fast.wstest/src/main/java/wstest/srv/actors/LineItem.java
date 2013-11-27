
package wstest.srv.actors;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="LineItem">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="OrderId" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:int" name="ItemId" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:int" name="ProductId" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="ProductDescription" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:int" name="OrderQuantity" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:float" name="UnitPrice" minOccurs="1" maxOccurs="1"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class LineItem
{
    private int orderId;
    private int itemId;
    private int productId;
    private String productDescription;
    private int orderQuantity;
    private Float unitPrice;

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
     * Get the 'ItemId' element value.
     * 
     * @return value
     */
    public int getItemId() {
        return itemId;
    }

    /** 
     * Set the 'ItemId' element value.
     * 
     * @param itemId
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /** 
     * Get the 'ProductId' element value.
     * 
     * @return value
     */
    public int getProductId() {
        return productId;
    }

    /** 
     * Set the 'ProductId' element value.
     * 
     * @param productId
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /** 
     * Get the 'ProductDescription' element value.
     * 
     * @return value
     */
    public String getProductDescription() {
        return productDescription;
    }

    /** 
     * Set the 'ProductDescription' element value.
     * 
     * @param productDescription
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /** 
     * Get the 'OrderQuantity' element value.
     * 
     * @return value
     */
    public int getOrderQuantity() {
        return orderQuantity;
    }

    /** 
     * Set the 'OrderQuantity' element value.
     * 
     * @param orderQuantity
     */
    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    /** 
     * Get the 'UnitPrice' element value.
     * 
     * @return value
     */
    public Float getUnitPrice() {
        return unitPrice;
    }

    /** 
     * Set the 'UnitPrice' element value.
     * 
     * @param unitPrice
     */
    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }
}
