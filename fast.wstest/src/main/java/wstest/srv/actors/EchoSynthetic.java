
package wstest.srv.actors;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:element xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="EchoSynthetic">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="ns:Synthetic" name="synth" minOccurs="0" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class EchoSynthetic {
  private Synthetic synth;

  /**
   * Get the 'synth' element value.
   * 
   * @return value
   */
  public Synthetic getSynth() {
    return synth;
  }

  /**
   * Set the 'synth' element value.
   * 
   * @param synth
   */
  public void setSynth(Synthetic synth) {
    this.synth = synth;
  }
}
