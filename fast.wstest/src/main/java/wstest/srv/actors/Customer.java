
package wstest.srv.actors;

import java.util.Date;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Customer">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="CustomerId" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="ContactFirstName" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="ContactLastName" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="ContactPhone" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:dateTime" name="LastActivityDate" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="CreditCardNumber" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="CreditCardExpirationDate" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="ns:Address" name="BillingAddress" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="ns:Address" name="ShippingAddress" minOccurs="1" maxOccurs="1"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Customer
{
    private int customerId;
    private String contactFirstName;
    private String contactLastName;
    private String contactPhone;
    private Date lastActivityDate;
    private String creditCardNumber;
    private String creditCardExpirationDate;
    private Address billingAddress;
    private Address shippingAddress;

    /** 
     * Get the 'CustomerId' element value.
     * 
     * @return value
     */
    public int getCustomerId() {
        return customerId;
    }

    /** 
     * Set the 'CustomerId' element value.
     * 
     * @param customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** 
     * Get the 'ContactFirstName' element value.
     * 
     * @return value
     */
    public String getContactFirstName() {
        return contactFirstName;
    }

    /** 
     * Set the 'ContactFirstName' element value.
     * 
     * @param contactFirstName
     */
    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    /** 
     * Get the 'ContactLastName' element value.
     * 
     * @return value
     */
    public String getContactLastName() {
        return contactLastName;
    }

    /** 
     * Set the 'ContactLastName' element value.
     * 
     * @param contactLastName
     */
    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    /** 
     * Get the 'ContactPhone' element value.
     * 
     * @return value
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /** 
     * Set the 'ContactPhone' element value.
     * 
     * @param contactPhone
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /** 
     * Get the 'LastActivityDate' element value.
     * 
     * @return value
     */
    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    /** 
     * Set the 'LastActivityDate' element value.
     * 
     * @param lastActivityDate
     */
    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    /** 
     * Get the 'CreditCardNumber' element value.
     * 
     * @return value
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /** 
     * Set the 'CreditCardNumber' element value.
     * 
     * @param creditCardNumber
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /** 
     * Get the 'CreditCardExpirationDate' element value.
     * 
     * @return value
     */
    public String getCreditCardExpirationDate() {
        return creditCardExpirationDate;
    }

    /** 
     * Set the 'CreditCardExpirationDate' element value.
     * 
     * @param creditCardExpirationDate
     */
    public void setCreditCardExpirationDate(String creditCardExpirationDate) {
        this.creditCardExpirationDate = creditCardExpirationDate;
    }

    /** 
     * Get the 'BillingAddress' element value.
     * 
     * @return value
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /** 
     * Set the 'BillingAddress' element value.
     * 
     * @param billingAddress
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    /** 
     * Get the 'ShippingAddress' element value.
     * 
     * @return value
     */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    /** 
     * Set the 'ShippingAddress' element value.
     * 
     * @param shippingAddress
     */
    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
