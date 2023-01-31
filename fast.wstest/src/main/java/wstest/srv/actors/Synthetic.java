
package wstest.srv.actors;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:complexType xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Synthetic">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="Strng" minOccurs="0" maxOccurs="1"/>
 *     &lt;xs:element type="ns:TestStruct" name="Strct" minOccurs="0" maxOccurs="1"/>
 *     &lt;xs:element type="xs:base64Binary" name="Barray" minOccurs="0" maxOccurs="1"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Synthetic {
  private String strng;
  private TestStruct strct;
  private byte[] barray;

  /**
   * Get the 'Strng' element value.
   * 
   * @return value
   */
  public String getStrng() {
    return strng;
  }

  /**
   * Set the 'Strng' element value.
   * 
   * @param strng
   */
  public void setStrng(String strng) {
    this.strng = strng;
  }

  /**
   * Get the 'Strct' element value.
   * 
   * @return value
   */
  public TestStruct getStrct() {
    return strct;
  }

  /**
   * Set the 'Strct' element value.
   * 
   * @param strct
   */
  public void setStrct(TestStruct strct) {
    this.strct = strct;
  }

  /**
   * Get the 'Barray' element value.
   * 
   * @return value
   */
  public byte[] getBarray() {
    return barray;
  }

  /**
   * Set the 'Barray' element value.
   * 
   * @param barray
   */
  public void setBarray(byte[] barray) {
    this.barray = barray;
  }
}
