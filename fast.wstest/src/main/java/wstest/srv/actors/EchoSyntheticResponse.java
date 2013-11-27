
package wstest.srv.actors;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="EchoSyntheticResponse">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="ns:Synthetic" name="EchoSyntheticResult" minOccurs="0" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class EchoSyntheticResponse
{
    private Synthetic echoSyntheticResult;

    /** 
     * Get the 'EchoSyntheticResult' element value.
     * 
     * @return value
     */
    public Synthetic getEchoSyntheticResult() {
        return echoSyntheticResult;
    }

    /** 
     * Set the 'EchoSyntheticResult' element value.
     * 
     * @param echoSyntheticResult
     */
    public void setEchoSyntheticResult(Synthetic echoSyntheticResult) {
        this.echoSyntheticResult = echoSyntheticResult;
    }
}
