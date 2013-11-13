
package wstest.srv.actors;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="EchoStructResponse">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="ns:COfTestStruct" name="EchoStructResult" minOccurs="1" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class EchoStructResponse
{
    private COfTestStruct echoStructResult;

    /** 
     * Get the 'EchoStructResult' element value.
     * 
     * @return value
     */
    public COfTestStruct getEchoStructResult() {
        return echoStructResult;
    }

    /** 
     * Set the 'EchoStructResult' element value.
     * 
     * @param echoStructResult
     */
    public void setEchoStructResult(COfTestStruct echoStructResult) {
        this.echoStructResult = echoStructResult;
    }
}
