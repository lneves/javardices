
package wstest.srv.actors;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="EchoListResponse">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="ns:TestNode" name="EchoListResult" minOccurs="0" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class EchoListResponse
{
    private TestNode echoListResult;

    /** 
     * Get the 'EchoListResult' element value.
     * 
     * @return value
     */
    public TestNode getEchoListResult() {
        return echoListResult;
    }

    /** 
     * Set the 'EchoListResult' element value.
     * 
     * @param echoListResult
     */
    public void setEchoListResult(TestNode echoListResult) {
        this.echoListResult = echoListResult;
    }
}
