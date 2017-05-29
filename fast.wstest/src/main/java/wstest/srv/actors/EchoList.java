
package wstest.srv.actors;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:element xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="EchoList">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="ns:TestNode" name="list" minOccurs="0" maxOccurs="1"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class EchoList
{
	private TestNode list;

	/**
	 * Get the 'list' element value.
	 * 
	 * @return value
	 */
	public TestNode getList()
	{
		return list;
	}

	/**
	 * Set the 'list' element value.
	 * 
	 * @param list
	 */
	public void setList(TestNode list)
	{
		this.list = list;
	}
}
