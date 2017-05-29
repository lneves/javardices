
package wstest.srv.actors;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:complexType xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="TestNode">
 *   &lt;!-- Reference to class wstest.srv.actors.TestNode -->
 * &lt;/xs:complexType>
 * </pre>
 */
public class TestNode extends TestStruct
{
	private TestNode next;

	/**
	 * Get the 'Next' element value.
	 * 
	 * @return value
	 */
	public TestNode getNext()
	{
		return next;
	}

	/**
	 * Set the 'Next' element value.
	 * 
	 * @param next
	 */
	public void setNext(TestNode next)
	{
		this.next = next;
	}
}
