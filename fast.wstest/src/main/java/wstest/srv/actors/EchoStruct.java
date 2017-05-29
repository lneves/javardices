
package wstest.srv.actors;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:element xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="EchoStruct">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="ns:COfTestStruct" name="array"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class EchoStruct
{
	private COfTestStruct array;

	/**
	 * Get the 'array' element value.
	 * 
	 * @return value
	 */
	public COfTestStruct getArray()
	{
		return array;
	}

	/**
	 * Set the 'array' element value.
	 * 
	 * @param array
	 */
	public void setArray(COfTestStruct array)
	{
		this.array = array;
	}
}
