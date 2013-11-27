
package wstest.srv.actors;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Address">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="FirstName" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="LastName" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="Address1" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="Address2" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="City" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="State" minOccurs="1" maxOccurs="1"/>
 *     &lt;xs:element type="xs:string" name="Zip" minOccurs="1" maxOccurs="1"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Address
{
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;

    /** 
     * Get the 'FirstName' element value.
     * 
     * @return value
     */
    public String getFirstName() {
        return firstName;
    }

    /** 
     * Set the 'FirstName' element value.
     * 
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** 
     * Get the 'LastName' element value.
     * 
     * @return value
     */
    public String getLastName() {
        return lastName;
    }

    /** 
     * Set the 'LastName' element value.
     * 
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** 
     * Get the 'Address1' element value.
     * 
     * @return value
     */
    public String getAddress1() {
        return address1;
    }

    /** 
     * Set the 'Address1' element value.
     * 
     * @param address1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /** 
     * Get the 'Address2' element value.
     * 
     * @return value
     */
    public String getAddress2() {
        return address2;
    }

    /** 
     * Set the 'Address2' element value.
     * 
     * @param address2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /** 
     * Get the 'City' element value.
     * 
     * @return value
     */
    public String getCity() {
        return city;
    }

    /** 
     * Set the 'City' element value.
     * 
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /** 
     * Get the 'State' element value.
     * 
     * @return value
     */
    public String getState() {
        return state;
    }

    /** 
     * Set the 'State' element value.
     * 
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /** 
     * Get the 'Zip' element value.
     * 
     * @return value
     */
    public String getZip() {
        return zip;
    }

    /** 
     * Set the 'Zip' element value.
     * 
     * @param zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }
}
